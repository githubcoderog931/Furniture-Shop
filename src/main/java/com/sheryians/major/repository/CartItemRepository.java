package com.sheryians.major.repository;

import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}
