package com.sheryians.major.Util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

//import com.example.openpdf.entity.Student;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class PDFGenerator {

    @Autowired
    UserService userService;

    @Autowired
    OrderRepository orderRepository;

    // List to hold all Students
    private List<Orders> studentList;

    public void generate(HttpServletResponse response , String name) throws DocumentException, IOException {



        User user = userService.findByUsername(name);
        List<Orders> orderList = orderRepository.findByUser(user);

        // Creating the Object of Document
        Document document = new Document(PageSize.A4);

        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());

        // Opening the created document to modify it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(20);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("List Of Orders", fontTiltle);

        // Aligning the paragraph in document
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        // Adding the created paragraph in document
        document.add(paragraph);

        // Creating a table of 3 columns
        PdfPTable table = new PdfPTable(3);

        // Setting width of table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] { 3, 3, 3 });
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();

        // Setting the background color and padding
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order Status", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Section", font));
        table.addCell(cell);

        // Iterating over the list of students
        for (Orders orders : orderList) {
            // Adding student id
            table.addCell(String.valueOf(orders.getId()));
            // Adding student name
            table.addCell(String.valueOf(orders.getOrderStatus()));
            // Adding student section
            table.addCell(String.valueOf(orders.getAmount()));
        }
        // Adding the created table to document
        document.add(table);

        // Closing the document
        document.close();

    }
}