package com.sheryians.major.repository;

import com.sheryians.major.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface OrderStatusRepository extends JpaRepository<OrderStatus,Long> {
}
