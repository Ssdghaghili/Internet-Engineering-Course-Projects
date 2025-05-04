package org.example.repository;

import org.example.model.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, Long> {
}
