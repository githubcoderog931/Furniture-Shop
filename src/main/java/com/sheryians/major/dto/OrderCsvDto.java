package com.sheryians.major.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderCsvDto {

    private String username;
    private String orderId;
    private LocalDateTime orderDate;
    private String status;
    private Double totalPrice;
    private String paymentMode;
    private String productName;
    private Double totalSales;
    private int totalOrders;
}