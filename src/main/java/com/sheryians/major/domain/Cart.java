package com.sheryians.major.domain;
import lombok.Data;

import javax.persistence.*;
import	java.math.BigDecimal;
import	java.util.HashMap;
import java.util.HashSet;
import	java.util.Map;
import java.util.Set;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id") // Name of the foreign key column in the Cart table
    private User user;

    @OneToMany(mappedBy = "cart")
    private Set<CartItem> cartItems = new HashSet<>();

    private BigDecimal grandTotal;

    @ManyToMany
    @JoinTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    public void calculateCartTotal() {
        // Calculate the total cart value here
    }

    // Constructors, getters, and setters
}

