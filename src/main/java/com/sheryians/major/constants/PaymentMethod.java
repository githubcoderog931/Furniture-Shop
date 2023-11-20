package com.sheryians.major.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PaymentMethod {

    PAY_ON_DELIVERY("pay on delivery"),
    RAZORPAY("razorpay"),
    WALLET("wallet payment");

    private final String value;

    public String getMethod(){
        return value;
    }
}
