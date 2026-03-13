package com.example.POD.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class JwtEntity {
    private String token;
    private String type = "Bearer";
    private String userEmail;

    private String userName;
    private Long userId;
    private String userRole;
}