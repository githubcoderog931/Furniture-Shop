package com.sheryians.major.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
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

    @OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
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

//    public User(User user) {
//        this.firstname = user.getFirstname();
//        this.lastname = user.getLastname();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//        this.roles = user.getRoles();
//        this.enable = user.getEnable();
//        this.cart = user.getCart();
//        this.addresses = user.getAddresses();
//        this.cartItem = (List<CartItem>) user.getCartItem();
//
//    }


    // define getters/setters



    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public CartItem getCartItem() {
        return (CartItem) cartItem;
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

