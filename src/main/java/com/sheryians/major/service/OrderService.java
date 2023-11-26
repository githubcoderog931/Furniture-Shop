package com.sheryians.major.service;

import com.sheryians.major.domain.*;
import com.sheryians.major.repository.AddressRepository;
import com.sheryians.major.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Service
public class OrderService {

    // define autowire

    @Autowired
    public OrderRepository orderRepository;



    // create an order

    public void placeOrder(Orders orders){
        orderRepository.save(orders);
    }


    // get all the orders

    public List<Orders> getAllOrders(User user){
        return orderRepository.findByUser(user);
    }

    // delete an order

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    // update orders

    public void updateOrder(Orders orders){
        orderRepository.save(orders);
    }


    public List<Orders> getAllOrderDate() {
        return orderRepository.findAll();
    }

    public List<Orders> getAllOrderPrice() {
        return orderRepository.findAll();
    }
}