package com.sheryians.major.service;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    public Double applyDiscount(Double totalPrice, Integer discount) {
        // Ensure discount is not null and is within a valid range
        if (discount == null || discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        // Calculate the discounted price
        double discountMultiplier = 1.0 - (discount.doubleValue() / 100.0);
        double discountedPrice = totalPrice * discountMultiplier;

        // Ensure the discounted price is not negative
        return Math.max(discountedPrice, 0.0);
    }
}
