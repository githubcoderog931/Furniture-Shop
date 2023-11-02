package com.sheryians.major.dto;

import lombok.Data;

import javax.persistence.*;


@Data
public class ProductDTO {

    private Long id;
    @Column(unique = true)
    private String name;
    private int categoryId;
    private double price;
    private double weight;
    private String description;
    private String imageName;
    private	long	unitsInStock;

}
