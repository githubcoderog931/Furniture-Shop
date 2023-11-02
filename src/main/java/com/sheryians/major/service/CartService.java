package com.sheryians.major.service;

import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.domain.User;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart addToCart(User user, Long productId, int quantity) {
        // Retrieve the user's cart
        quantity = 1;
        Cart cart = user.getCart();

        if (cart == null) {
            // If the user doesn't have a cart, create a new one
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        // Retrieve the product from the database
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Check if the product is already in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // If the product is already in the cart, update the quantity
            CartItem cartItem = existingCartItem.get();
            quantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        // Recalculate the cart's total
        cart.calculateCartTotal();

        // Save the updated cart to the database
        cartRepository.save(cart);

        return cart;
    }


    public Cart getCartForUser(String username){
        return cartRepository.findByUser_Email(username);
    }



}


