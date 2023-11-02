package com.sheryians.major.domain;


import javax.persistence.*;




@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    // define fields

    @ManyToOne
    @JoinColumn(name = "user_id_fk")
    private User user;


    @Column(name = "user_name")
    private String userName;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "state")
    private String state;

    @Column(name = "default_address")
    private boolean isDefault;

    // define constructors

    public Address(){

    }

    public Address(User user, String userName, String street, String city, String zipCode, String state, boolean isDefault) {
        this.user = user;
        this.userName = userName;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.isDefault = isDefault;
    }


    // define getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }


    // define toString


    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", user=" + user +
                ", userName='" + userName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", state='" + state + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
