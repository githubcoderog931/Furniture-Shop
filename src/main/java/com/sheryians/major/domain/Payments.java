package com.sheryians.major.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    Long id;

    @Column(name = "razorpay_id")
    String razorpayPaymentId;

    @Column(name = "razor_order_id")
    String razorpayOrderId;

    @Column(name = "razor_signature")
    String razorpaySignature;

    @Column(name = "amount")
    Float amount;



}
