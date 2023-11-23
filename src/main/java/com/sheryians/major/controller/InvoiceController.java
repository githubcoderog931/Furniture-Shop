package com.sheryians.major.controller;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sheryians.major.domain.Invoice;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import com.sheryians.major.dto.OrderCsvDto;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.OrderService;
import com.sheryians.major.service.PdfInvoiceService;
import com.sheryians.major.service.SalesReportService;

import com.sheryians.major.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class InvoiceController {

    @Autowired
    private PdfInvoiceService pdfInvoiceService;

    @Autowired
    UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SalesReportService salesReportService;


    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/generate-invoice/{orderId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateInvoice(@PathVariable("orderId") Long orderId, Principal principal) {
        try {
            // Fetch the order by ID and convert it to an Invoice

            // Generate PDF based on the Invoice
            byte[] pdfContent = pdfInvoiceService.generateInvoicePdf(orderId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice.pdf");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (DocumentException e) {
            // Handle exceptions appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin
    @GetMapping("/salesReportPDF")
    public ResponseEntity<byte[]> generateSalesReportPDF(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        System.out.println(startDate);
        System.out.println(endDate);

        try {

            String title = "Sales Report";
            int totalOrderCount = 100; // Replace with your actual data
            double totalRevenue = 5000.0; // Replace with your actual data
            Map<LocalDate, Long> dailyOrderCount = new HashMap<>(); // Replace with your actual data

            byte[] pdfBytes = salesReportService.generateSalesReportPDFBytes(title, startDate, endDate,
                    totalOrderCount, totalRevenue, dailyOrderCount);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("salesReport.pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/salesReportCsv")
    public ResponseEntity<byte[]> generateCSVReport( Principal principal ,HttpServletResponse response, Model model)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        // Create a temporary file to store the CSV data
        File tempFile = File.createTempFile("salesReport", ".csv");
        List<Orders> orders = orderRepository.findAll();
        try (Writer writer = new FileWriter(tempFile)) {
            StatefulBeanToCsv<OrderCsvDto> csvWriter = new StatefulBeanToCsvBuilder<OrderCsvDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(true)
                    .build();

            List<OrderCsvDto> orderCsvDtoList = new ArrayList<>();
            for (Orders order : orders) {
                String productName = order.getOrderItems().stream()
                        .map(orderItem -> orderItem.getProduct().getName())
                        .collect(Collectors.joining());

                OrderCsvDto orderCsvDto = new OrderCsvDto();
                orderCsvDto.setOrderId(String.valueOf(order.getId()));
                orderCsvDto.setUsername(order.getUser().getEmail());
                orderCsvDto.setTotalPrice((double) order.getAmount());
                orderCsvDto.setOrderDate(order.getLocalDate());
                orderCsvDto.setPaymentMode(String.valueOf(order.getPaymentMethod()));
                orderCsvDto.setStatus(String.valueOf(order.getOrderStatus()));
                orderCsvDto.setProductName(productName);

                orderCsvDtoList.add(orderCsvDto);
            }

            csvWriter.write(orderCsvDtoList);

            Double totalSales = salesReportService.calculateTotalSales(orders);
            writer.write("TOTAL SALES," + totalSales + "\n");

            int totalOrderCount = orders.size();
            writer.write("TOTAL ORDER COUNT," + totalOrderCount + "\n");
        }

        // Convert the temporary file to a byte array
        byte[] bytes = FileUtils.readFileToByteArray(tempFile);

        // Set the content type and headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "salesReport.csv");

        // Return the file as a byte array along with headers
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }



}
