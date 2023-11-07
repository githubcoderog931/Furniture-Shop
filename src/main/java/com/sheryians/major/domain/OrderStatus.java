package com.sheryians.major.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String status;
    @ManyToOne
    private Orders order;



    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
