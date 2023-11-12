package com.sheryians.major.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {


    // define fields

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    @OneToOne
    @JoinColumn(name = "user_id_fk",unique = true)
    private User user;

    @OneToMany(mappedBy = "cart")
    @Column(name = "cart_item_fk")
    private List<CartItem> cartItems;

//    @ManyToMany
//    @JoinTable(
//            name = "cart_product",
//            joinColumns = @JoinColumn(name = "cart_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id")
//    )
    public double calculateCartTotal() {
        double grandTotal = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Double itemTotal = (product != null && product.getPrice() != null)
                    ? product.getPrice() * cartItem.getQuantity()
                    : 0.0;

            grandTotal += itemTotal;
        }

        return grandTotal;
    }
    public  double discountPrice;


    // define constructors

    public Cart(){

    }

    public Cart(User user, List<CartItem> cartItems) {
        this.user = user;
        this.cartItems = cartItems;
    }


    // define getters/setters


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

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }


    // define to string

//    @Override
//    public String toString() {
//        return "Cart{" +
//                "id=" + id +
//                ", user=" + user +
//                ", cartItems=" + cartItems +
//                '}';
//    }


//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Cart cart = (Cart) o;
//        return Objects.equals(id, cart.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }


}
