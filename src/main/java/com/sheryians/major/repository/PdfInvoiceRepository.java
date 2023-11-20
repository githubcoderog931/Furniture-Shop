package com.sheryians.major.repository;

import com.sheryians.major.domain.Invoice;
import com.sheryians.major.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PdfInvoiceRepository extends JpaRepository<Invoice,Long> {
    Invoice findByOrders(Orders orders);
}
