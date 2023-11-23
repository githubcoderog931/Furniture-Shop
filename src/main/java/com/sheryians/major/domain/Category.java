package com.sheryians.major.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "category")
public class Category {

    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "category", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "offer_discount")
    public Integer offerDiscount;

    // define constructors

    public Category(){

    }

    public Category(String category, String description) {
        this.name = category;
        this.description = description;
    }


    // define getters/setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return name;
    }

    public void setCategory(String category) {
        this.name = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // define toString

//    @Override
//    public String toString() {
//        return "Category{" +
//                "id=" + id +
//                ", category='" + name + '\'' +
//                ", description='" + description + '\'' +
//                '}';
//    }
}