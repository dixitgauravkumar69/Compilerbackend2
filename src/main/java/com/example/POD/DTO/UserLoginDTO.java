package com.example.POD.DTO;

import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserLoginDTO {


    private String userEmail;
    private String password;


}
