package com.sheryians.major.service;

import com.sheryians.major.domain.Coupon;
import org.springframework.stereotype.Service;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Service
public class CouponService {



    public boolean couponIsApplicable(Coupon coupon, double totalPrice){
        LocalDate currentDate = LocalDate.now();
        return coupon!=null &&
                !currentDate.isAfter(coupon.getExpiryDate()) &&
                coupon.getCouponStock() > 0 && totalPrice >=coupon.getMaxAmount();
    }

}
