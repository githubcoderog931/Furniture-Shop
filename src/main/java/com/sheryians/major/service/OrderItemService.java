package com.sheryians.major.service;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.OrderItem;
import com.sheryians.major.domain.Orders;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;


    public void saveOrderItem(OrderItem orderItem){
        orderItemRepository.save(orderItem);
    }

    public List<OrderItem> moveItemsFromCartToOrder(String name, Orders orders){
        Cart cart = cartRepository.findByUser_Email(name);
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrders(orders);
            orderItem.setProduct(cartItem.getProduct());
            orderItems.add(orderItem);
            long stock = cartItem.getProduct().getUnitsInStock();
            long quantity = cartItem.getQuantity();
            cartItem.getProduct().setUnitsInStock(stock-quantity);
        }
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(cartItems);
        return savedOrderItems;
    }
}
