package com.example.POD.Service;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForgetPasswordService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;// for saving token temporarly

    private final UserRepository userRepository;
    private final EmailService emailService;
    public String generateResetLink(String email,String origin)
    {
        UserEntity user =userRepository.findByUserEmail(email);

        String token="";
        String resetLink="";

        if(user==null)
        {
            return "PLease enter a valid and actual email..";
        }
        else
        {
            token=this.generateToken(email);
           resetLink=this.generateLink(token,origin);


           //Send mail in user account mail........
            emailService.sendResetLinkToUser(email,resetLink);

        }

        return resetLink;
    }


    //generate and save token in redis
    private String generateToken(String email)
    {
        //Token generation......................
        String token = UUID.randomUUID().toString();

        //Token save kr li redis me 15 minutes ke liye...........
        redisTemplate.opsForValue().set(
                token,
                email,
                Duration.ofMinutes(15)
        );
        return token;
    }

    private String generateLink(String token,String origin)
    {

        String resetLink = origin +"/reset-password?token=" + token;
        return resetLink;
    }
}
