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
        orderItemsTable.addCell(headslNoCell);

        PdfPCell headProductNameCell = new PdfPCell(new Phrase("Order Items "));
        headProductNameCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headProductNameCell.setFixedHeight(40f); // Set the height as needed
        orderItemsTable.addCell(headProductNameCell);

        PdfPCell headQuantityCell = new PdfPCell(new Phrase("Quantity"));
        headQuantityCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headQuantityCell.setFixedHeight(40f); // Set the height as needed
        orderItemsTable.addCell(headQuantityCell);

        PdfPCell headPriceCell = new PdfPCell(new Phrase("Price"));
        headPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
        headPriceCell.setFixedHeight(40f); // Set the height as needed
        orderItemsTable.addCell(headPriceCell);

// ... (rest of the code)

        Integer slNo = 1;
        for (OrderItem orderItem : orders.getOrderItems()) {
            PdfPCell slNoCell = new PdfPCell(new Phrase(slNo + " \n"));
            slNoCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            slNoCell.setVerticalAlignment(Element.ALIGN_CENTER);
            slNoCell.setFixedHeight(40f); // Set the height as needed
            orderItemsTable.addCell(slNoCell);

            PdfPCell productNameCell = new PdfPCell(new Phrase(orderItem.getProduct().getName() + "\n"));
            productNameCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            productNameCell.setVerticalAlignment(Element.ALIGN_CENTER);
            productNameCell.setFixedHeight(40f); // Set the height as needed
            orderItemsTable.addCell(productNameCell);

            PdfPCell quantityCell = new PdfPCell(new Phrase(orderItem.getQuantity() + "\n"));
            quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            quantityCell.setVerticalAlignment(Element.ALIGN_CENTER);
            quantityCell.setFixedHeight(40f); // Set the height as needed
            orderItemsTable.addCell(quantityCell);

            PdfPCell priceCell = new PdfPCell(new Phrase(orderItem.getProduct().getPrice() + "\n"));
            priceCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Adjust the alignment as needed
            priceCell.setVerticalAlignment(Element.ALIGN_CENTER);
            priceCell.setFixedHeight(40f); // Set the height as needed
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

        PdfPCell subtotalValueCell = new PdfPCell(new Phrase(String.valueOf(orders.getDiscountAmount())));
        subtotalValueCell.setFixedHeight(cellHeight);
        subtotalValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(subtotalValueCell);

        PdfPCell shippingCell = new PdfPCell(new Phrase("Shipping:"));
        shippingCell.setFixedHeight(cellHeight); // Set the height as needed
        shippingCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(shippingCell);

        PdfPCell shippingValueCell = new PdfPCell(new Phrase("0.00"));
        shippingValueCell.setFixedHeight(cellHeight);
        shippingValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        costTable.addCell(shippingValueCell);

//        costTable.setSpacingAfter(20); // Adjust the value as needed for your desired bottom margin

        document.add(costTable);

        PdfPTable total = new PdfPTable(2);
        total.setWidthPercentage(40);
        total.setHorizontalAlignment(Element.ALIGN_LEFT);



        PdfPCell totalCell = new PdfPCell(new Phrase("total:"));
        totalCell.setFixedHeight(cellHeight);
        totalCell.setBorder(Rectangle.NO_BORDER); // Remove border
        total.addCell(totalCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(String.valueOf(orders.getAmount())));
        totalValueCell.setFixedHeight(cellHeight);
        totalValueCell.setBorder(Rectangle.NO_BORDER); // Remove border
        total.addCell(totalValueCell);



        document.add(total);

        Paragraph billingInfo = new Paragraph("Billing Information", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        billingInfo.setAlignment(Element.ALIGN_LEFT);
        billingInfo.setSpacingAfter(10); // Adjust the value as needed for your desired bottom margin
        document.add(billingInfo);

        PdfPTable billing = new PdfPTable(2);
        billing.setHorizontalAlignment(Element.ALIGN_LEFT);
        billing.setWidthPercentage(100);
        billing.setWidthPercentage(50);



        PdfPCell name = new PdfPCell(new Phrase("Name:"));
        name.setFixedHeight(cellHeight);
        name.setBorder(Rectangle.NO_BORDER);
        billing.addCell(name);

        PdfPCell nameValue = new PdfPCell(new Phrase(orders.getUser().getFirstname()+" "+orders.getUser().getLastname()));
        nameValue.setFixedHeight(cellHeight);
        nameValue.setBorder(Rectangle.NO_BORDER);
        billing.addCell(nameValue);

        PdfPCell address = new PdfPCell(new Phrase("Address :"));
        address.setFixedHeight(cellHeight);
        address.setBorder(Rectangle.NO_BORDER);
        billing.addCell(address);

        PdfPCell addressValue = new PdfPCell(new Phrase(orders.getAddress().getCity()+" "+orders.getAddress().getStreet()+" "+orders.getAddress().getState()+" "+orders.getAddress().getZipCode()));
        addressValue.setFixedHeight(cellHeight);
        addressValue.setBorder(Rectangle.NO_BORDER);
        billing.addCell(addressValue);

        PdfPCell payment = new PdfPCell(new Phrase("Payment method :"));
        payment.setFixedHeight(cellHeight);
        payment.setBorder(Rectangle.NO_BORDER);
        billing.addCell(payment);

        PdfPCell paymentMethod = new PdfPCell(new Phrase(orders.getPaymentMethod().getValue()));
        paymentMethod.setFixedHeight(cellHeight);
        paymentMethod.setBorder(Rectangle.NO_BORDER);
        billing.addCell(paymentMethod);

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
        totalAmountTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalAmountTable.setWidthPercentage(50);
        totalAmountTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        totalAmountTable.addCell("Discount Amount:");
        totalAmountTable.addCell(String.valueOf(orders.getDiscountAmount())); // Hardcoded total amount for demonstration
        document.add(totalAmountTable);


        total.setSpacingAfter(20); // Adjust the value as needed for your desired bottom margin

        Double savedAmt = orders.getAmount()-orders.getDiscountAmount();
        document.add(total);
        Paragraph saved = new Paragraph("You saved "+savedAmt+" on this order!!");
        Paragraph thanks = new Paragraph("Thank you for your order!\n");
        thanks.setAlignment(Element.ALIGN_LEFT);
        thanks.setSpacingAfter(10); // Adjust the value as needed for your desired bottom margin
        document.add(saved);

        document.add(thanks);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }
}
