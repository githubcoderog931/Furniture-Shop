package com.sheryians.major.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name",nullable = false)
    @NotEmpty(message = "{First name must not be empty}")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "email",nullable = false, unique = true)
    @NotEmpty
    @Email(message = "{error.invalid_email}")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
    )
    private List<Role> roles;

    @Column(name = "user_enabled")
    private Boolean enable;

    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToMany(mappedBy = "user")
    @Column(name = "address_id_fk")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user")
    @Column(name = "cart_items_fk")
    private List<CartItem> cartItem;

    private boolean Active;
    private boolean verified;
    private String otp;
    private LocalDateTime otpGeneratedTime;


    // define constructors

    public User(){

    }

    public User(User user) {
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.enable = user.getEnable();
        this.cart = user.getCart();
        this.addresses = user.getAddresses();
        this.cartItem = (List<CartItem>) user.getCartItem();

    }


    // define getters/setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public CartItem getCartItem() {
        return (CartItem) cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = (List<CartItem>) cartItem;
    }

    // define toString

//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", firstname='" + firstname + '\'' +
//                ", lastname='" + lastname + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", roles=" + roles +
//                ", enable=" + enable +
//                ", cart=" + cart +
//                ", addresses=" + addresses +
//                '}';
//    }
}

