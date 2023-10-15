package com.sheryians.major.domain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;


@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    // Constructors, getters, and setters

}