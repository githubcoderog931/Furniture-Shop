package com.sheryians.major.repository;

import com.sheryians.major.domain.Orders;
import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUser(User user);



}