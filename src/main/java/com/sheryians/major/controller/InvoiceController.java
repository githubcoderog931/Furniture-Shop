package com.sheryians.major.controller;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.exceptions.CsvException;
import com.sheryians.major.domain.Invoice;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.OrderService;
import com.sheryians.major.service.PdfInvoiceService;
import com.sheryians.major.service.SalesReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class InvoiceController {

    @Autowired
    private PdfInvoiceService pdfInvoiceService;

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

    public static void generateSalesReportPDF(String filename, String title, LocalDate startDate, LocalDate endDate,
                                              int totalOrderCount, double totalRevenue, Map<LocalDate, Long> dailyOrderCount)
            throws DocumentException, IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        // Add title and other information to the PDF
        document.add(new Paragraph(title));
        document.add(new Paragraph("Date Range: " + startDate + " to " + endDate));
        document.add(new Paragraph("Total Order Count: " + totalOrderCount));
        document.add(new Paragraph("Total Revenue: $" + totalRevenue));

        // Add daily order counts to the PDF
        PdfPTable table = new PdfPTable(2);
        table.addCell("Date");
        table.addCell("Daily Order Count");

        for (Map.Entry<LocalDate, Long> entry : dailyOrderCount.entrySet()) {
            table.addCell(entry.getKey().toString());
            table.addCell(entry.getValue().toString());
        }

        document.add(table);

        document.close();
    }



    @GetMapping("/csv")
    public ResponseEntity<byte[]> generateCSVReport(Model model) {
        try {
            // Example data
            LocalDate startDate = LocalDate.now().minusDays(30);
            LocalDate endDate = LocalDate.now();
            int totalOrderCount = 100;
            double totalRevenue = 50000.0;
            Map<LocalDate, Long> dailyOrderCount = Map.of(
                    LocalDate.now().minusDays(30), 10L,
                    LocalDate.now().minusDays(29), 15L,
                    LocalDate.now().minusDays(28), 20L
                    // ... additional daily order counts
            );

            // Generate CSV report
            byte[] csvBytes = salesReportService.generateSalesReportCSVBytes(
                    "Sales Report for One Month", startDate, endDate, totalOrderCount, totalRevenue, dailyOrderCount);

            // Set up response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "SalesReportOneMonth.csv");

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
