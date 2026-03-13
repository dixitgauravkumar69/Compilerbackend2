package com.example.POD.Controller;

import com.example.POD.DTO.JwtEntity;
import com.example.POD.DTO.UserDTO;
import com.example.POD.DTO.UserLoginDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import com.example.POD.Service.UserService;
import com.example.POD.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/User")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepo;


    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired JwtUtils jwtUtils;

    @PostMapping("/addUser")
    public UserDTO addUser(@RequestBody UserDTO user)
    {
       UserDTO createdUser= userService.addUser(user);
       return createdUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        // 1. Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getUserEmail(), userLoginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Generate Token
        String jwt = jwtUtils.generateJwtToken(userLoginDTO.getUserEmail());

        UserEntity user = userRepo.findByUserEmail(userLoginDTO.getUserEmail());
        // 3. Return Token and Email
        return ResponseEntity.ok(new JwtEntity(
                jwt,
                "Bearer",
                user.getUserEmail(),
                user.getUsername(),
                user.getUserid(),
                user.getUserRole()
        ));

    }

    @GetMapping("/getProblemStatements")
    public List<ProblemStatement> getProblemStatements()
    {
      List< ProblemStatement>  ps= userService.getProblemStatements();
       return ps;
    }

    @GetMapping("/profile")
    public UserEntity getProfile(@RequestParam String email)
    {
        UserEntity user=userRepo.findByUserEmail(email);
        return user;
    }
}
