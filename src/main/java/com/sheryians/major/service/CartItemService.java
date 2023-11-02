package com.sheryians.major.service;


import com.sheryians.major.domain.Cart;
import com.sheryians.major.domain.CartItem;
import com.sheryians.major.domain.Product;
import com.sheryians.major.repository.CartItemRepository;
import com.sheryians.major.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService{

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;



    public void deleteCartItemById(Long itemId){
        cartItemRepository.deleteById(itemId);
    }

    public List<CartItem> getAllItems(Cart cart) {
        // Query the database to fetch all cart items associated with the provided cart
        return cartItemRepository.findByCart(cart);
    }

    public CartItem findCartItemByProduct(Product product) {
        return cartItemRepository.findByProduct(product);
    }


    public void saveCartItem(CartItem item){
        cartItemRepository.save(item);
    }


    // get units in stock

    public long unitsInStockOfProduct(Product productId){
        CartItem cartItem = cartItemRepository.findByProduct(productId);
        if (cartItem != null) {
            Product product = productRepository.findById(productId.getId()).orElse(null);
            if (product != null) {
                return product.getUnitsInStock();
            }
        }


        return 0;
    }


}
