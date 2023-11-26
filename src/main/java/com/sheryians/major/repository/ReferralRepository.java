package com.sheryians.major.repository;

import com.sheryians.major.domain.Referral;
import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    Referral findByReferrerAndCompleted(User referrer, boolean b);

    boolean existsByReferrerAndReferred(User referrer, User referred);

    Referral findByReferrer(User user);

    Referral findByReferralCode(String code);

    Referral findByReferred(User user);

    List<Referral> findByCompleted(Boolean complete);

}
