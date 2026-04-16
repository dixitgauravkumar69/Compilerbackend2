package com.example.POD.Controller;

import com.example.POD.DTO.ResetPasswordDTO;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import com.example.POD.Service.ForgetPasswordService;
import com.example.POD.Service.ResetPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/User")
@RequiredArgsConstructor
public class ForgetPassword {
    private final ForgetPasswordService forgetPasswordService;
    private final ResetPasswordService resetPasswordService;
    @PostMapping("/forget/password/{email}")
    public String forgetPassword(@PathVariable String email, HttpServletRequest request)
    {
        String origin = request.getHeader("Origin");
        return forgetPasswordService.generateResetLink(email,origin);

    }

    @PatchMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordDTO resetDTO)
    {
      return resetPasswordService.validateToken(resetDTO.getToken(),resetDTO.getNewPassword());
    }


}
