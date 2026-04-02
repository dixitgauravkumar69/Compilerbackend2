package com.example.POD.Controller;

import com.example.POD.DTO.JwtEntity;
import com.example.POD.DTO.UserDTO;
import com.example.POD.DTO.UserLoginDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import com.example.POD.Service.EmailService;
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
//@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/User")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepo;
   private final EmailService emailService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired JwtUtils jwtUtils;

    @PostMapping("/addUser")
    public UserEntity addUser(@RequestBody UserDTO user) {
        //  Pehle user ko database mein save krunga
        UserEntity createdUser = userService.addUser(user);


        if (createdUser != null && createdUser.getUserEmail() != null) {
            // Mail ke liye rukenge nhi vo background me jata rhega
            emailService.sendWelcomeEmail(createdUser.getUserEmail(), createdUser.getUsername());
        }

        return createdUser;
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            // 1. Authenticate (email + password)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getUserEmail(),
                            userLoginDTO.getPassword()
                    )
            );

            // 2. Set authentication
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Fetch user from DB
            UserEntity user = userRepo.findByUserEmail(userLoginDTO.getUserEmail());

            if (user == null) {
                return ResponseEntity.status(404).body("User Not Found");
            }

            // 4. Generate JWT
            String jwt = jwtUtils.generateJwtToken(
                    user.getUserEmail(),
                    user.getUserRole()
            );

            // 5. Return response
            return ResponseEntity.ok(new JwtEntity(
                    jwt,
                    "Bearer",
                    user.getUserEmail(),
                    user.getUsername(),
                    user.getUserid(),
                    user.getUserRole()
            ));

        } catch (Exception e) {

            return ResponseEntity.status(401).body("Invalid Email or Password");
        }
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
