package com.sheryians.major.controller;

import com.sheryians.major.domain.Orders;
import com.sheryians.major.repository.OrderRepository;
import com.sheryians.major.repository.OrderStatusRepository;
import com.sheryians.major.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    OrderRepository orderRepository;


    @GetMapping("/admin/orders")
    public String getAdminOrders(Model model) {

        model.addAttribute("userOrder", orderRepository.findAll());
        return "adminOrders";
    }


    /////////////orderStatusManagement///////////////////


    @GetMapping("/admin/order/cancel/{id}")
    public String getCancelOrder(@PathVariable long id) {

        Orders orders = orderRepository.findById(id).get();

        orders.setOrderStatus(orderStatusRepository.findById(5L).get());

        orderRepository.save(orders);


        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/shipped/{id}")
    public String getShippedOrder(@PathVariable long id) {

        Orders orders = orderRepository.findById(id).get();

        orders.setOrderStatus(orderStatusRepository.findById(2L).get());

        orderRepository.save(orders);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/pending/{id}")
    public String getPendingOrder(@PathVariable long id) {

        Orders orders = orderRepository.findById(id).get();
        orders.setOrderStatus(orderStatusRepository.findById(6L).get());
        orderRepository.save(orders);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/transit/{id}")
    public String getTransitOrder(@PathVariable long id) {

        Orders orders = orderRepository.findById(id).get();
        orders.setOrderStatus(orderStatusRepository.findById(3L).get());
        orderRepository.save(orders);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/delivered/{id}")
    public String getDeliveredOrder(@PathVariable long id) {

        Orders orders = orderRepository.findById(id).get();
        orders.setOrderStatus(orderStatusRepository.findById(4L).get());
        orderRepository.save(orders);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/order/confirmed/{id}")
    public String getConfirmedOrder(@PathVariable long id) {
        Orders orders = orderRepository.findById(id).get();
        orders.setOrderStatus(orderStatusRepository.findById(1L).get());
        orderRepository.save(orders);
        return "redirect:/admin/orders";
    }
}
