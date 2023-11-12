package com.sheryians.major.domain;

import com.razorpay.Order;
import lombok.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_method")
    private String paymentMethod;

    @OneToMany(mappedBy = "paymentMethod")
    private List<Orders> orders;


}
