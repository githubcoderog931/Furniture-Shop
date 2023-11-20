package com.sheryians.major.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@AllArgsConstructor
public enum OrderStatus {

    ORDER("order confirmed"),
    PENDING("order pending"),
    TRANSIT("order in transit"),
    SHIPPED("order shipped"),
    DELIVERED("order delivered"),
    CANCELLED("order cancelled"),
    RETURNED("order returned");

    private final String value;
    public String getStatus() {
        return value;
    }


}
