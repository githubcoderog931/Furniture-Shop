package com.sheryians.major.domain;


import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
public class ProductImage {


    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "image_name")
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    //define constructors

    public ProductImage(){

    }

    public ProductImage(String imageUrl, Product product) {
        this.imageUrl = imageUrl;
        this.product = product;
    }


    //define getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
