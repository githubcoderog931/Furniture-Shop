package com.sheryians.major.domain;

import lombok.*;

import javax.persistence.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @ManyToOne
    @JoinColumn(name = "referred_id")
    private User referred;

    private String referralCode;

    private boolean completed;

    private boolean referrerPurchase;

    private boolean referredPurchase;

    // getters and setters
}
