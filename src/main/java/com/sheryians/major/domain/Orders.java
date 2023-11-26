package com.sheryians.major.domain;

import javax.persistence.*;

import com.sheryians.major.constants.OrderStatus;
import com.sheryians.major.constants.PaymentMethod;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Orders {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    private Double discountAmount;

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
    private LocalDateTime localDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime shippingDate;
    private LocalDateTime packingDate;
    private LocalDateTime deliveryDate;



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