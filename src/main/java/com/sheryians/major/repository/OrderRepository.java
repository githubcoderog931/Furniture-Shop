package com.sheryians.major.repository;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUser(User user);
    List<Orders> findByLocalDate(LocalDate date);
    List<Orders> findByLocalDateBetween(LocalDate startDate, LocalDate endDate);

    Long countByOrderStatus(OrderStatus status);



}