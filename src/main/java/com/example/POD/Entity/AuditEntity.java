package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 50)
    private String action;

    // Optional reason (for audit clarity)

    private String reason;

    // User on whom action is performed
    @Column(name = "affected_user_id", nullable = false)
    private Long userId;

    // Timestamp when record created
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Long updatedBy;
    // Auto set before insert
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}