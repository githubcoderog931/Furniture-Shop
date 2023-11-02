package com.sheryians.major.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {


    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;


    // define constructors

    public OrderItem(){

    }

    public OrderItem(int quantity, Product product, Orders orders) {
        this.quantity = quantity;
        this.product = product;
        this.orders = orders;
    }


    // define getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }


    // define toString

//    @Override
//    public String toString() {
//        return "OrderItem{" +
//                "id=" + id +
//                ", quantity=" + quantity +
//                ", product=" + product +
//                ", orders=" + orders +
//                '}';
//    }
}
