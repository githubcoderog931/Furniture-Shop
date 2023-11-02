package com.sheryians.major.repository;


import com.sheryians.major.domain.OrderItem;
import com.sheryians.major.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem , Long> {
    public List<OrderItem> findByOrders(Orders orders);
}
