package com.sheryians.major.domain;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public	class	Product	{


    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "product_name", unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String imageName;

    @Column(name = "units_in_stock")
    @Min(value = 0, message = "Units in stock cannot be negative")
    private	long	unitsInStock;

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    @Column(name = "offer_discount")
    public Integer offerDiscount;

    // define constructors

    public Product(){

    }

    public Product(String name, Category category, Double price, String description, String imageName, long unitsInStock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.unitsInStock = unitsInStock;
    }


    // define getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(long unitsInStock) {
        this.unitsInStock = unitsInStock;
    }


    // define toString

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageName='" + imageName + '\'' +
                ", unitsInStock=" + unitsInStock +
                '}';
    }
}

