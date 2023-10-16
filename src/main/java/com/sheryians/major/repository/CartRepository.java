package com.sheryians.major.repository;

import com.sheryians.major.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser_Email(String username);
}
