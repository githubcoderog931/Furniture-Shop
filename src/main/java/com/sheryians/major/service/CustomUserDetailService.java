package com.sheryians.major.service;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CustomUserDetail;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired

    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!user.isVerified()) {
            throw new RuntimeException("User not found");
        }

        // Check if the user has a cart; if not, create one
        if (user.getCart() == null) {

            Cart cart = new Cart();

            cart.setUser(user); // Associate the cart with the user
            cartRepository.save(cart);

            user.setCart(cart); // Associate the user with the cart
            userRepository.save(user); // Save the user and cart to the database


        }

        return new CustomUserDetail(user);
    }


}