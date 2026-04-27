package com.example.POD.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AuditViewFullDTO {
    private String action;
    private String reason;
    private Long userId;
    private String effectedUser;
    private LocalDateTime ActionDate;
    private Long updatedBy;
    private String updatedByName;
}
