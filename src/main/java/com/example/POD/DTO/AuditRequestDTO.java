package com.example.POD.DTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AuditRequestDTO {
    private String action;
    private String reason;
}
