package com.sheryians.major.repository;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUser(User user);
    List<Orders> findByLocalDate(LocalDateTime date);
    List<Orders> findByLocalDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countByOrderStatus(OrderStatus status);

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.localDate >= :startOfWeek AND o.localDate <= :endOfWeek GROUP BY o.localDate")
    List<Long> getOrderCountsByDateRange(@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);




}