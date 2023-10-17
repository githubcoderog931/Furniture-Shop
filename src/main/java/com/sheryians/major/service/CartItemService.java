package com.sheryians.major.service;


import com.sheryians.major.domain.CartItem;
import com.sheryians.major.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService{

    @Autowired
    CartItemRepository cartItemRepository;
    public void deleteCartItemById(Long itemId){
        cartItemRepository.deleteById(itemId);
    }

    public List<CartItem> getAllItems(){
        return cartItemRepository.findAll();
    }
}
