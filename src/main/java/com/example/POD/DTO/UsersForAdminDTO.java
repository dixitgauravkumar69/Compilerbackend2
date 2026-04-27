package com.example.POD.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UsersForAdminDTO {

    private String username;


    private String userRole;


    private String userEmail;

    private String status;
    private Long userId;

}
