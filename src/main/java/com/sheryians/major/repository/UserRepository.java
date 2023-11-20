package com.sheryians.major.repository;



import com.sheryians.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
    User findByEmail(String email);
    User findByReferralCode(String referralCode);
}

