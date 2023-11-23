package com.sheryians.major.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sheryians.major.domain.Invoice;
import com.sheryians.major.domain.OrderItem;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.repository.PdfInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfInvoiceService {

    @Autowired
    PdfInvoiceRepository pdfInvoiceRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserService userService;




    public byte[] generateInvoicePdf(Long orderId) throws DocumentException {
        Orders orders = orderRepository.findById(orderId).orElse(null);

        // Sample hardcoded values for demonstration
        String companyName = "FurniSure";


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();

        // Header
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell(new Phrase(companyName));
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(headerCell);
        document.add(headerTable);

        // Address Section
        PdfPTable addressTable = new PdfPTable(1);
        addressTable.setWidthPercentage(100);
        PdfPCell addressCell = new PdfPCell(new Phrase("To: " + orders.getAddress().getUserName()+" \n\n"+ "Address : "+ "\n\n" +
                orders.getAddress().getState()+" "+orders.getAddress().getCity() +" \n"+orders.getAddress().getStreet()+" "+
                orders.getAddress().getZipCode()+"\n\n\n\n"));
        addressCell.setBorder(Rectangle.NO_BORDER);
        addressTable.addCell(addressCell);
        document.add(addressTable);

        // Order Items Section
        PdfPTable orderItemsTable = new PdfPTable(4);
        orderItemsTable.setWidthPercentage(100);
        orderItemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        // Set minimum height for each cell

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

//        orderItemsTable.addCell("Orders id: ");
        orderItemsTable.addCell("Sl.no ");
        orderItemsTable.addCell("Order Items ");
        orderItemsTable.addCell("Quantity");
        orderItemsTable.addCell("Price");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

//        orderItemsTable.addCell(String.valueOf(orderId)); // Orders id

        int slNo = 1;
        for(OrderItem orderItem : orders.getOrderItems()){

            orderItemsTable.addCell(slNo+" \n"); // Sl.no for Product A
            orderItemsTable.addCell(orderItem.getProduct().getName()+"\n");
            orderItemsTable.addCell(orderItem.getQuantity()+"\n");
            orderItemsTable.addCell(orderItem.getProduct().getPrice()+"\n");
            slNo++;
        }


        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");

        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");
        orderItemsTable.addCell(" ");


        document.add(orderItemsTable);

        // Total Amount Section
        PdfPTable totalAmountTable = new PdfPTable(2);
        totalAmountTable.setWidthPercentage(100);
        orderItemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        totalAmountTable.addCell("Total Amount:");
        totalAmountTable.addCell(String.valueOf(orders.getAmount()+"₹")); // Hardcoded total amount for demonstration
        document.add(totalAmountTable);

        // Footer
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);
        PdfPCell footerCell = new PdfPCell(new Phrase("Thank you for your business!"));
        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerCell.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(footerCell);
        document.add(footerTable);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
