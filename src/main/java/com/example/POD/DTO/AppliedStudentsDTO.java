package com.example.POD.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class AppliedStudentsDTO {
    private String name;
    private String email;
    private String branch;
    private LocalDateTime appliedDate;
}
