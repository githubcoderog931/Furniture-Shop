package com.sheryians.major.domain;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Entity
@Data
@Table(name = "orders")
public class Orders {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "amount")
    private int amount;

    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id_fk")
    private Address address;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @Column(name = "orders_id_fk")
    private List<OrderItem> orderItems;

    @Column(name = "order_placed_date")
    private LocalDate localDate;

    @ManyToOne
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id") // Specify the column name in the Orders table
    private PaymentMethod paymentMethod;



    // define constructors

    public Orders() {

    }

    public Orders(int amount, User user, Address address, List<OrderItem> orderItems, LocalDate localDate, OrderStatus orderStatus) {
        this.amount = amount;
        this.user = user;
        this.address = address;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.localDate = localDate;
    }


    // define getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }


    // define toString

//    @Override
//    public String toString() {
//        return "Orders{" +
//                "id=" + id +
//                ", amount=" + amount +
//                ", user=" + user +
//                ", address=" + address +
//                ", orderItems=" + orderItems +
//                ", localDate=" + localDate +
//                '}';
//    }
}