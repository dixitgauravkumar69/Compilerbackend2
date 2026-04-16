package com.example.POD.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class ResetPasswordDTO {
    private String newPassword;
    private String token;

}
