//package com.sheryians.major.domain;
//
//
//import lombok.Getter;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//@Entity
//@Table(name = "otp")
//public class Otp {
//    @Getter
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Getter
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
//
//
//    @Column(name = "otp_value", length = 6)
//    private String otpValue;
//
//    @Getter
//    @Column(name = "expiration_time")
//    private Date expirationTime;
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public void setOtpValue(String otpValue) {
//        this.otpValue = otpValue;
//    }
//
//    public void setExpirationTime(Date expirationTime) {
//        this.expirationTime = expirationTime;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public String getOtpValue() {
//        return otpValue;
//    }
//
//    public Date getExpirationTime() {
//        return expirationTime;
//    }
//// Getters and setters
//}
