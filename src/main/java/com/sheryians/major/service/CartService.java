package com.sheryians.major.service;

import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.CartRepository;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository; // Replace with your actual Cart repository
    @Autowired
    private ProductRepository productRepository; // Replace with your actual Product repository

    public void addToCart(String username,Long cartId, Long productId, int quantity) {
        // Retrieve the cart from the database
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        // Retrieve the product from the database
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Check if the product is already in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // If the product is already in the cart, update the quantity
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(newQuantity);
        } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

        // Recalculate the cart's total
        cart.calculateCartTotal();

        // Save the updated cart to the database
        cartRepository.save(cart);
    }
}
