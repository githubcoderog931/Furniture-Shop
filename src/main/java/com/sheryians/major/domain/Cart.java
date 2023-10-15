package com.sheryians.major.domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String cartId;
    @OneToMany
    private String ProductId;
    
}
