package com.sheryians.major.service;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesReportService {

    @Autowired
    OrderRepository orderRepository;

    public List<Orders> getOrderByTimePeriod(TimePeriod timePeriod){

        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

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

    public List<Orders> findByOrderDateBetween(LocalDate startDate, LocalDate endDate){
        return orderRepository.findByLocalDateBetween(startDate,endDate);
    }

    public SalesDto getSalesForOneDay(){

        LocalDate yesterday = LocalDate.now();
        LocalDate today = LocalDate.now();

        List<Orders> ordersWithInOneDay = orderRepository.findByLocalDateBetween(yesterday,today).stream().filter(orders ->
                orders.getOrderStatus()!= OrderStatus.CANCELLED).toList();

        Map<LocalDate, Long> dailyOrderCount = ordersWithInOneDay.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        Double totalRevenue = ordersWithInOneDay.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneDay.size();

        return getSalesDto(yesterday, today, totalRevenue, totalOrderCount,dailyOrderCount);
    }


    @NotNull
    private static SalesDto getDto(LocalDate yesterday,
                                   LocalDate today, Double totalRevenue, Integer totalOrderCount,Map<LocalDate, Long> dailyOrderCount) {
        return getSalesDto(yesterday, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }

    public SalesDto getSalesForOneMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        LocalDate today = LocalDate.now();

        List<Orders> ordersWithInOneMonth = orderRepository.findByLocalDateBetween(oneMonthAgo,today).stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Double totalRevenue = ordersWithInOneMonth.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneMonth.size();
        Map<LocalDate,Long> dailyOrderCount = ordersWithInOneMonth.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        return getSalesDto(oneMonthAgo, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }

    @NotNull
    private static SalesDto getSalesDto(LocalDate oneMonthAgo, LocalDate today, Double totalRevenue, Integer totalOrderCount, Map<LocalDate, Long> dailyOrderCount) {
        SalesDto salesDto = new SalesDto();
        salesDto.setStartDate(oneMonthAgo);
        salesDto.setEndDate(today);
        salesDto.setTotalOrderCount(totalOrderCount);
        salesDto.setTotalRevenue(totalRevenue);
        salesDto.setDailyOrderCounts(dailyOrderCount);
        return salesDto;
    }

    public SalesDto getSalesForOneYear() {

        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        LocalDate today = LocalDate.now();

        List<Orders> ordersWithInOneYear = orderRepository.findByLocalDateBetween(oneYearAgo,today).stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Double totalRevenue = ordersWithInOneYear.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneYear.size();
        Map<LocalDate,Long> dailyOrderCount = ordersWithInOneYear.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        return getSalesDto(oneYearAgo, today, totalRevenue, totalOrderCount,dailyOrderCount);
    }


    public SalesDto getSalesForAllTime() {

        List<Orders> totalSales = orderRepository.findAll().stream().filter(order ->
                order.getOrderStatus()!=OrderStatus.CANCELLED).toList();
        Map<String ,Double>monthlySales = new HashMap<>();

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (Orders order : totalSales){
            LocalDate orderDate = order.getLocalDate();
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

        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        LocalDate today = LocalDate.now();

        List<Orders> ordersWithInOneWeek = orderRepository.findByLocalDateBetween(oneWeekAgo,today).
                stream().filter(order ->
                        order.getOrderStatus()!=OrderStatus.CANCELLED).toList();

        Map<LocalDate,Long> dailyOrderCount = ordersWithInOneWeek.stream()
                .collect(Collectors.groupingBy(Orders::getLocalDate,Collectors.counting()));

        Double totalRevenue = ordersWithInOneWeek.stream().mapToDouble(Orders::getAmount).sum();
        Integer totalOrderCount = ordersWithInOneWeek.size();

        return getSalesDto(oneWeekAgo, today, totalRevenue, totalOrderCount, dailyOrderCount);

    }

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

}
