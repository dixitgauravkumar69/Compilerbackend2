package com.example.POD.Controller.Admin;

import com.example.POD.DTO.UsersForAdminDTO;
import com.example.POD.Service.AllUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/admin")


public class AllUsersController {
    private final AllUsersService allUsersService;
    @GetMapping("/getAllUsers")
    public List<UsersForAdminDTO> allUsers()
    {
        return allUsersService.allUsers();
    }
}
