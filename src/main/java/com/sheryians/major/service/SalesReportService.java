package com.sheryians.major.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.TimePeriod;
import com.sheryians.major.dto.SalesDto;
import com.sheryians.major.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesReportService {

    @Autowired
    OrderRepository orderRepository;

    public List<Orders> getOrderByTimePeriod(TimePeriod timePeriod){

        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (timePeriod){
            case DAILY -> {
                startDate = endDate;
                break;
            }
            case WEEKLY -> {
                startDate = endDate.minusDays(6);
                break;
            }
            case MONTHLY -> {
                startDate = endDate.minusMonths(1);
                break;
            }
            default -> {
                throw new IllegalArgumentException("unsupported time period "+timePeriod);
            }
        }
        return orderRepository.findByLocalDateBetween(startDate,endDate);
    }

    public List<Orders> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate){
        return orderRepository.findByLocalDateBetween(startDate,endDate);
    }

    public SalesDto getSalesForOneDay(){

        LocalDateTime yesterday = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.now();

        List<Orders> ordersWithInOneDay = orderRepository.findByLocalDateBetween(yesterday,today).stream().filter(orders ->
                orders.getOrderStatus()!= OrderStatus.CANCELLED).toList();

        Map<LocalDateTime, Long> dailyOrderCount = ordersWithInOneDay.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        Double totalRevenue = ordersWithInOneDay.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneDay.size();

        return getSalesDto(yesterday, today, totalRevenue, totalOrderCount,dailyOrderCount);
    }


    @NotNull
    private static SalesDto getDto(LocalDateTime yesterday,
                                   LocalDateTime today, Double totalRevenue, Integer totalOrderCount,Map<LocalDateTime, Long> dailyOrderCount) {
        return getSalesDto(yesterday, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }

    public SalesDto getSalesForOneMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        LocalDateTime today = LocalDateTime.now();

        List<Orders> ordersWithInOneMonth = orderRepository.findByLocalDateBetween(oneMonthAgo,today).stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Double totalRevenue = ordersWithInOneMonth.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneMonth.size();
        Map<LocalDateTime,Long> dailyOrderCount = ordersWithInOneMonth.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        return getSalesDto(oneMonthAgo, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }

    @NotNull
    private static SalesDto getSalesDto(LocalDateTime oneMonthAgo, LocalDateTime today, Double totalRevenue, Integer totalOrderCount, Map<LocalDateTime, Long> dailyOrderCount) {
        SalesDto salesDto = new SalesDto();
        salesDto.setStartDate(oneMonthAgo);
        salesDto.setEndDate(today);
        salesDto.setTotalOrderCount(totalOrderCount);
        salesDto.setTotalRevenue(totalRevenue);
        salesDto.setDailyOrderCounts(dailyOrderCount);
        return salesDto;
    }

    public SalesDto getSalesForOneYear() {

        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        LocalDateTime today = LocalDateTime.now();

        List<Orders> ordersWithInOneYear = orderRepository.findByLocalDateBetween(oneYearAgo,today).stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Double totalRevenue = ordersWithInOneYear.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneYear.size();
        Map<LocalDateTime,Long> dailyOrderCount = ordersWithInOneYear.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        return getSalesDto(oneYearAgo, today, totalRevenue, totalOrderCount,dailyOrderCount);
    }


    public SalesDto getSalesForAllTime() {

        List<Orders> totalSales = orderRepository.findAll().stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();
        Map<String ,Double>monthlySales = new HashMap<>();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (Orders order : totalSales){
            LocalDateTime orderDate = order.getLocalDate();
            String monthYear = orderDate.format(monthFormatter);
            double totalPrice = order.getAmount();

            monthlySales.put(monthYear,monthlySales.getOrDefault(monthYear, 0.0)+totalPrice);
        }
        List<String> months = new ArrayList<>(monthlySales.keySet());
        List<Double> amount  = new ArrayList<>(monthlySales.values());

        Double totalRevenue = amount.stream().mapToDouble(Double::doubleValue).sum();
        Integer totalCount = totalSales.size();

        return getSalesDto(months, amount, totalRevenue, totalCount);

    }

    @NotNull
    private static SalesDto getSalesDto(List<String> months, List<Double> amount,
                                        Double totalRevenue, Integer totalCount) {
        SalesDto salesDto = new SalesDto();
        salesDto.setTotalOrderCount(totalCount);
        salesDto.setTotalRevenue(totalRevenue);
        salesDto.setMonthlySales(months, amount);
        return salesDto;
    }

    public List<OrderStatus> getStatus() {
        List<Orders> orders = orderRepository.findAll();
        return orders.stream().map(Orders::getOrderStatus).toList();

    }

    public Map<OrderStatus, Long> getStatusCounts() {
        List<OrderStatus> statusList = getStatus();
        Map<OrderStatus,Long> statusCount = new HashMap<>();
        for (OrderStatus status : statusList){
            Long count = orderRepository.countByOrderStatus(status);
            statusCount.put(status,count);
        }
        return statusCount;
    }


    public SalesDto getSalesForOneWeek() {

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        LocalDateTime today = LocalDateTime.now();

        List<Orders> ordersWithInOneWeek = orderRepository.findByLocalDateBetween(oneWeekAgo,today).
                stream().filter(order ->
                        order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Map<LocalDateTime,Long> dailyOrderCount = ordersWithInOneWeek.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        Double totalRevenue = ordersWithInOneWeek.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneWeek.size();

        return getSalesDto(oneWeekAgo, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }



//    public List<Orders> getWeeklySales() {
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        LocalDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
//            List<Orders> orderList = orderRepository.findByLocalDateBetween(startOfWeek, endOfWeek);
//            for(Orders orders : orderList){
//
//            }
//            return ""
//
//
//
//
//    }



    public Double calculateTotalSales(List<Orders> orders) {

        double totalPrice = 0;
        for (Orders order : orders){
            totalPrice += order.getAmount();
        }
        return totalPrice;
    }

    public byte[] generateSalesReportCSVBytes(String title, LocalDate startDate, LocalDate endDate,
                                              int totalOrderCount, double totalRevenue, Map<LocalDate, Long> dailyOrderCount)
            throws IOException {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream))) {

            // Write data to CSV
            csvWriter.writeNext(new String[]{"Date", "Daily Order Count"});
            for (Map.Entry<LocalDate, Long> entry : dailyOrderCount.entrySet()) {
                csvWriter.writeNext(new String[]{entry.getKey().toString(), entry.getValue().toString()});
            }

            // Dummy implementation for testing
            System.out.println("Generating CSV report:");
            System.out.println("Title: " + title);
            System.out.println("Date Range: " + startDate + " to " + endDate);
            System.out.println("Total Order Count: " + totalOrderCount);
            System.out.println("Total Revenue: $" + totalRevenue);

            csvWriter.flush();  // Flush the CSV writer to ensure data is written to the stream
            return outputStream.toByteArray();
        }
    }

    public byte[] generateSalesReportPDFBytes(String title, LocalDateTime startDate, LocalDateTime endDate,
                                              int totalOrderCount, double totalRevenue, Map<LocalDateTime, Long> dailyOrderCount)
            throws DocumentException, IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Add metadata
        document.addTitle("Sales Report");
        document.addSubject("Sales Report");
        document.addKeywords("sales, report, PDF");
        document.addAuthor("Your Name");
        document.addCreator("Your Application");

        // Add content to the PDF
        addSalesReportContent(document, title, startDate, endDate, totalOrderCount, totalRevenue, dailyOrderCount);

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void addSalesReportContent(Document document, String title, LocalDateTime startDate, LocalDateTime endDate,
                                       int totalOrderCount, double totalRevenue, Map<LocalDateTime, Long> dailyOrderCount)
            throws DocumentException {
        List<Orders> orders = orderRepository.findAll();
        // Add title
        Paragraph titleParagraph = new Paragraph(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph);

        // Add date range
        Paragraph dateRangeParagraph = new Paragraph("Date Range: " + startDate + " to " + endDate);
        dateRangeParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(dateRangeParagraph);

        // Sort orders by date
        orders.sort(Comparator.comparing(Orders::getLocalDate));

        // Add total order count and revenue
        document.add(new Paragraph("Total Order Count: " + orders.size()+"\n\n"));
        totalRevenue = orders.stream().mapToDouble(Orders::getAmount).sum();
        document.add(new Paragraph("Total Revenue: " + totalRevenue+"\n\n\n\n\n"));

        // Add individual orders
        document.add(new Paragraph("Individual Orders: "+"\n\n\n\n"));

        String orderInfo = "Order ID   " +
                "  User   " +
                "  Order Date  " +
                "  Total Price   "  +
                "  Payment Method   "  +
                "  Order Status   ";
        document.add(new Paragraph(orderInfo));

        for (Orders order : orders) {
            if (order.getLocalDate() != null && order.getLocalDate().isAfter(startDate) && order.getLocalDate().isBefore(endDate)) {
                orderInfo = order.getId() +"   "+
                 order.getUser().getEmail() +"  "+
                  order.getLocalDate() +"   "+
                 order.getAmount() +"   "+
                  order.getPaymentMethod() +"   " +
                 order.getOrderStatus();
                document.add(new Paragraph(orderInfo));

            }
        }
    }

}
