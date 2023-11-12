package com.sheryians.major.repository;


import com.sheryians.major.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCouponCode(String couponCode);
}
