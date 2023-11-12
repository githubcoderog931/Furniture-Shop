package com.sheryians.major.repository;

import com.sheryians.major.domain.User;
import com.sheryians.major.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Wallet findByUser(User user);

}
