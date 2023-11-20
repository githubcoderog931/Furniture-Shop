package com.sheryians.major.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    private String userName;

    @OneToOne
    private Orders orders;


    @ManyToOne
    @JoinColumn(name = "address_id_fk")
    private Address address;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @Column(name = "orders_id_fk")
    private List<OrderItem> orderItems;
    private LocalDate orderPlacedDate;
    private String orderStatus;
    private String paymentMethod;
    private LocalDate shippingDate;
    private LocalDate packingDate;
    private LocalDate deliveryDate;

    // Add getters and setters

    public Invoice(Orders order) {
        this.id = order.getId();
        this.amount = order.getAmount();
        this.userName = order.getUser().getEmail();
        this.address = order.getAddress();
        this.orderItems = order.getOrderItems();
        this.orderPlacedDate = order.getLocalDate();
        this.orderStatus = order.getOrderStatus().name();
        this.paymentMethod = order.getPaymentMethod().name();
        this.shippingDate = order.getShippingDate();
        this.packingDate = order.getPackingDate();
        this.deliveryDate = order.getDeliveryDate();
    }
}
