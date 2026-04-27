package com.example.POD.Service;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    @Autowired
    RedisTemplate<String,String>redisTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public String validateToken(String token,String newPassword)
    {
       String email=redisTemplate.opsForValue().get(token);

        if (email == null || email.equals("null") || email.isEmpty()) {
            return "Token expired";
        }

        if(email != null){
            email = email.replace("\"", "").trim();
        }

        System.out.println("Email is : "+email);
        System.out.println(email.length());





      String updatedPassword =resetPassword(email,newPassword);

       //Jaise hi user password reset kare to usi time me y delete bhi ho jae vha se...

           redisTemplate.delete(token);


       return "Updated password is:"+ updatedPassword;

    }

    public String resetPassword(String email,String newPassword)
    {

        UserEntity user = userRepository.findByUserEmail(email);


        if(user==null)
        {
            return "User not found for this mail";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return  newPassword;

    }


}
