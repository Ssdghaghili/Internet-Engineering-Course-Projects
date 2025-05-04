package org.example.repository;

import org.example.model.PurchaseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {
}
