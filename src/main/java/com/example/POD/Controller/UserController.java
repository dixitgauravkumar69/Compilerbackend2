package com.example.POD.Controller;

import com.example.POD.DTO.ChangePasswordDTO;
import com.example.POD.DTO.JwtEntity;
import com.example.POD.DTO.UserDTO;
import com.example.POD.DTO.UserLoginDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import com.example.POD.Service.EmailService;
import com.example.POD.Service.OtpService;
import com.example.POD.Service.UserService;
import com.example.POD.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


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
    private final OtpService otpService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired JwtUtils jwtUtils;
    @Autowired  PasswordEncoder passwordEncoder;

    @PostMapping("/addUser")
    public ResponseEntity<String> requestOtp(@RequestBody UserDTO user) {
        // Check if user already exists
        if(userRepo.findByUserEmail(user.getUserEmail()) != null) {
            return ResponseEntity.status(409).body("User already exists");
        }

        // Generate OTP
        String otp = otpService.generateOtp();

        // Save OTP in cache/service
        otpService.saveOtp(user.getUserEmail(), otp);

        // Send OTP email
        emailService.sendWelcomeEmail(user.getUserEmail(), user.getUserName(), otp);

        return ResponseEntity.ok("OTP sent to email. Please verify to complete registration.");
    }

  @PatchMapping("/changePassword/{email}")
     public ResponseEntity changePassword(@PathVariable String email,@RequestBody ChangePasswordDTO changePass)
  {
      if(userRepo.findByUserEmail(email)==null)
             {
                 return ResponseEntity.status(404).body("USer not found");
             }
      return userService.ChangePassword(email,changePass.getOldPassword(),changePass.getNewPassword());

  }

    @PostMapping("/verifyOtpAndRegister")
    public ResponseEntity<?> verifyOtpAndRegister(@RequestBody UserDTO user, @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(user.getUserEmail(), otp);

        if(!isValid) {
            return ResponseEntity.status(400).body("Invalid OTP");
        }

        // OTP valid hai ab db me save kr lunga
        UserEntity createdUser = userService.addUser(user);

        //After saving user delete this otp
        otpService.deleteOtp(createdUser.getUserEmail());

        return ResponseEntity.ok(createdUser);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {

        try {
            //  Fetch user first
            UserEntity user = userRepo.findByUserEmail(userLoginDTO.getUserEmail());

            if (user == null) {
                return ResponseEntity.status(404).body("User Not Found");
            }


            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(401).body("Password null of db");
            }

            if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid Email or Password");
            }

            if (user.getStatus() != null &&
                    "DEACTIVATE".equalsIgnoreCase(user.getStatus().trim())) {

                return ResponseEntity.status(403).body(
                        Map.of("message", "Contact to admin YOU ARE BLOCKED IN THIS SYSTEM")
                );
            }


            if(("TEACHER".equals(user.getUserRole())) && (Boolean.FALSE.equals(user.getIsApproved())))
            {
                return ResponseEntity.status(403).body("Your request is not authorized by ADMIN");
            }






//             String encodedPassword=passwordEncoder.encode(userLoginDTO.getPassword());


            // Authenticate manually (since we already validated)
            // Safety check for role to avoid "Cannot pass null or empty values" error
            String rawRole = (user.getUserRole() == null || user.getUserRole().trim().isEmpty()) ? "USER" : user.getUserRole();
            
            // Ensure "ROLE_" prefix is added only once
            String roleWithPrefix = rawRole.startsWith("ROLE_") ? 
                                    rawRole : "ROLE_" + rawRole;

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getUserEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority(roleWithPrefix))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //  Generate JWT
            // Safety check for email to avoid JWT constructor errors
            if (user.getUserEmail() == null || user.getUserEmail().trim().isEmpty()) {
                throw new IllegalStateException("User found but has no valid email in DB");
            }

            String jwt = jwtUtils.generateJwtToken(
                    user.getUserEmail(),
                    roleWithPrefix
            );

            //  Return response
            return ResponseEntity.ok(new JwtEntity(
                    jwt,
                    "Bearer",
                    user.getUserEmail(),
                    user.getUsername(),
                    user.getUserid(),
                    roleWithPrefix
            ));

        } catch (Exception e) {
            // Return actual error message temporarily for debugging
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }


    @GetMapping("/getProblemStatements/{includeTestCase}")
    public List<ProblemStatement> getProblemStatements(@PathVariable boolean includeTestCase)
    {
      List< ProblemStatement>  ps= userService.getProblemStatements(includeTestCase);

       return ps;
    }



    @Cacheable(value = "profileEndpointCache")
    @GetMapping("/profile")
    public UserEntity getProfile(@RequestParam String email)
    {
        UserEntity user=userRepo.findByUserEmail(email);
        return user;
    }




    //For first time creation of Admin

    @PostMapping("/createAdmin")
    public UserEntity createAdmin(@RequestBody UserDTO user)
    {
       UserEntity createdUser=userService.addUser(user);
       return createdUser;
    }

}
