package com.example.POD.Controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;


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
            Object principal = authentication.getPrincipal();

            String email;

            if (principal instanceof UserDetails) {
                email = ((UserDetails) principal).getUsername();
            } else {
                email = principal.toString();
            }

            UserEntity user = userRepo.findByUserEmail(email);
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

            return ResponseEntity.status(401).body("Invalid Email or Password"+ e.getMessage());
        }
    }


    @GetMapping("/getProblemStatements")
    public List<ProblemStatement> getProblemStatements()
    {
      List< ProblemStatement>  ps= userService.getProblemStatements();
       return ps;
    }

    @Cacheable(value = "profileEndpointCache")
    @GetMapping("/profile")
    public UserEntity getProfile(@RequestParam String email)
    {
        UserEntity user=userRepo.findByUserEmail(email);
        return user;
    }
}
