package com.sheryians.major.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "coupon code is required")
    private String couponCode;

    @Column(name = "discount")
    private int discount;

    @Column(name = "max_amount")
    private int maxAmount;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate expiryDate;

    private int couponStock;

    private Double cartAmount;


}