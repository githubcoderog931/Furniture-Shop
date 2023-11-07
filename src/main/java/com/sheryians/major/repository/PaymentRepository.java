package com.sheryians.major.repository;

import com.sheryians.major.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, Long> {
}
