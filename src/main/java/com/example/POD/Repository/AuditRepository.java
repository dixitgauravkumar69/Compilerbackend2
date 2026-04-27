package com.example.POD.Repository;

import com.example.POD.Entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditEntity,Long> {
}
