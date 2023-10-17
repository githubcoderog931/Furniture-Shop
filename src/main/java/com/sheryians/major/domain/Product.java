package com.sheryians.major.domain;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Entity
public	class	Product	{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;
    private Double price;
    private double weight;
    private String description;
    private String imageName;
    private	String	manufacturer;
    private	long	unitsInStock;
    private	long	unitsInOrder;


}

