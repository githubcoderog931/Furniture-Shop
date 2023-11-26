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

        // Order Summary Heading
        Paragraph orderSummaryHeading = new Paragraph("Order Summary", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        orderSummaryHeading.setAlignment(Element.ALIGN_CENTER);
        orderSummaryHeading.setSpacingAfter(30); // Adjust the value as needed for your desired bottom margin
        document.add(orderSummaryHeading);
// Order Items Section
        PdfPTable orderItemsTable = new PdfPTable(4);
        orderItemsTable.setWidthPercentage(100);

        PdfPCell headslNoCell = new PdfPCell(new Phrase("Sl.no "));
        headslNoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headslNoCell.setFixedHeight(40f); // Set the height as needed
        headslNoCell.setBorder(Rectangle.NO_BORDER); // Remove border
        orderItemsTable.addCell(headslNoCell);

        PdfPCell headProductNameCell = new PdfPCell(new Phrase("Order Items "));
        headProductNameCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headProductNameCell.setFixedHeight(40f); // Set the height as needed
        headProductNameCell.setBorder(Rectangle.NO_BORDER); // Remove border
        orderItemsTable.addCell(headProductNameCell);

        PdfPCell headQuantityCell = new PdfPCell(new Phrase("Quantity"));
        headQuantityCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headQuantityCell.setFixedHeight(40f); // Set the height as needed
        headQuantityCell.setBorder(Rectangle.NO_BORDER); // Remove border
        orderItemsTable.addCell(headQuantityCell);

        PdfPCell headPriceCell = new PdfPCell(new Phrase("Price"));
        headPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headPriceCell.setFixedHeight(40f); // Set the height as needed
        headPriceCell.setBorder(Rectangle.NO_BORDER); // Remove border
        orderItemsTable.addCell(headPriceCell);

// ... (rest of the code)

        Integer slNo = 1;
        for (OrderItem orderItem : orders.getOrderItems()) {
            PdfPCell slNoCell = new PdfPCell(new Phrase(slNo + " \n"));
            slNoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            slNoCell.setFixedHeight(40f); // Set the height as needed
            slNoCell.setBorder(Rectangle.NO_BORDER); // Remove border
            orderItemsTable.addCell(slNoCell);

            PdfPCell productNameCell = new PdfPCell(new Phrase(orderItem.getProduct().getName() + "\n"));
            productNameCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            productNameCell.setFixedHeight(40f); // Set the height as needed
            productNameCell.setBorder(Rectangle.NO_BORDER); // Remove border
            orderItemsTable.addCell(productNameCell);

            PdfPCell quantityCell = new PdfPCell(new Phrase(orderItem.getQuantity() + "\n"));
            quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            quantityCell.setFixedHeight(40f); // Set the height as needed
            quantityCell.setBorder(Rectangle.NO_BORDER); // Remove border
            orderItemsTable.addCell(quantityCell);

            PdfPCell priceCell = new PdfPCell(new Phrase(orderItem.getProduct().getPrice() + "\n"));
            priceCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            priceCell.setFixedHeight(40f); // Set the height as needed
            priceCell.setBorder(Rectangle.NO_BORDER); // Remove border
            orderItemsTable.addCell(priceCell);

            slNo++;
        }

        document.add(orderItemsTable);

        // Subtotal, Shipping, and Tax Section
        PdfPTable costTable = new PdfPTable(2);
        costTable.setWidthPercentage(40);
        costTable.setHorizontalAlignment(Element.ALIGN_LEFT);


        float cellHeight = 30f; // Set the height of each cell as needed

        PdfPCell subtotalCell = new PdfPCell(new Phrase("Subtotal:"));
        subtotalCell.setFixedHeight(cellHeight);
        subtotalCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(subtotalCell);

        PdfPCell subtotalValueCell = new PdfPCell(new Phrase("$35.00"));
        subtotalValueCell.setFixedHeight(cellHeight);
        subtotalValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(subtotalValueCell);

        PdfPCell shippingCell = new PdfPCell(new Phrase("Shipping:"));
        shippingCell.setFixedHeight(cellHeight); // Set the height as needed
        shippingCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(shippingCell);

        PdfPCell shippingValueCell = new PdfPCell(new Phrase("$5.00"));
        shippingValueCell.setFixedHeight(cellHeight);
        shippingValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(shippingValueCell);

        costTable.setSpacingAfter(20); // Adjust the value as needed for your desired bottom margin

        document.add(costTable);

        PdfPTable total = new PdfPTable(1);
        costTable.setWidthPercentage(100);


        PdfPCell totalCell = new PdfPCell(new Phrase("total:"));
        subtotalCell.setFixedHeight(cellHeight);
        costTable.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase("$35.00"));
        subtotalValueCell.setFixedHeight(cellHeight);
        totalValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(totalValueCell);

        total.setSpacingAfter(20); // Adjust the value as needed for your desired bottom margin


        document.add(total);

        Paragraph billingInfo = new Paragraph("Billing Information", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        orderSummaryHeading.setAlignment(Element.ALIGN_LEFT);
        orderSummaryHeading.setSpacingAfter(10); // Adjust the value as needed for your desired bottom margin
        document.add(billingInfo);

        PdfPTable billing = new PdfPTable(3);
        costTable.setWidthPercentage(100);


        PdfPCell name = new PdfPCell(new Phrase("Name:"));
        subtotalCell.setFixedHeight(cellHeight);
        costTable.addCell(name);

        PdfPCell nameValue = new PdfPCell(new Phrase("Arjun"));
        subtotalValueCell.setFixedHeight(cellHeight);
        costTable.addCell(nameValue);

        PdfPCell address = new PdfPCell(new Phrase("Address"));
        subtotalCell.setFixedHeight(cellHeight);
        costTable.addCell(address);

        PdfPCell addressValue = new PdfPCell(new Phrase("in basement, under house ,123456"));
        subtotalValueCell.setFixedHeight(cellHeight);
        costTable.addCell(addressValue);

        PdfPCell payment = new PdfPCell(new Phrase("Payment method :"));
        subtotalCell.setFixedHeight(cellHeight);
        costTable.addCell(payment);

        PdfPCell paymentMethod = new PdfPCell(new Phrase("Cash on delivery"));
        subtotalValueCell.setFixedHeight(cellHeight);
        costTable.addCell(paymentMethod);

        billing.setSpacingAfter(20); // Adjust the value as needed for your desired bottom margin


        document.add(billing);



////        // Address Section
//        PdfPTable addressTable = new PdfPTable(1);
//        addressTable.setWidthPercentage(100);
//        PdfPCell addressCell = new PdfPCell(new Phrase("To: " + orders.getAddress().getUserName()+" \n\n"+ "Address : "+ "\n\n" +
//                orders.getAddress().getState()+" "+orders.getAddress().getCity() +" \n"+orders.getAddress().getStreet()+" "+
//                orders.getAddress().getZipCode()+"\n\n\n\n"));
//        addressCell.setBorder(Rectangle.NO_BORDER);
//        addressTable.addCell(addressCell);
//        document.add(addressTable);



        // Total Amount Section
        PdfPTable totalAmountTable = new PdfPTable(2);
        totalAmountTable.setWidthPercentage(100);
        orderItemsTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        totalAmountTable.addCell("Total Amount:");
        totalAmountTable.addCell(String.valueOf(orders.getAmount()+"₹")); // Hardcoded total amount for demonstration
        document.add(totalAmountTable);

        Paragraph thanks = new Paragraph("Thank you for your order!\n");
        thanks.setAlignment(Element.ALIGN_LEFT);
        thanks.setSpacingAfter(10); // Adjust the value as needed for your desired bottom margin
        document.add(thanks);
        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
