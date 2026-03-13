package com.example.POD.DTO;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String userId;
    private String userName;
    private String userRole;
    private String userEmail;
    private String password;
}
