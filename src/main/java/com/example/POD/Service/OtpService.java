package com.example.POD.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
    public class OtpService {

        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        //OTP generate with random + security 6 digit
        public String generateOtp() {
            int otp = 100000 + new Random().nextInt(900000); //
            return String.valueOf(otp);
        }

        // OTP will save in redis(IN memory me RAM) 5 Minute ke liye
        public void saveOtp(String email, String otp) {
            redisTemplate.opsForValue().set(email, otp, 5, TimeUnit.MINUTES);
            System.out.println("OTP saved successfully....");
        }

        //When user enter otp than verification of it....
        public boolean validateOtp(String email, String userOtp) {
            String storedOtp = (String) redisTemplate.opsForValue().get(email);

            System.out.println("Stored Otp is :"+ storedOtp);

            if (storedOtp == null) {
                return false; // expired or not exist
            }

            return storedOtp.equals(userOtp);
        }

        // After one cycle delete it also......
        public void deleteOtp(String email) {
            redisTemplate.delete(email);
        }
    }

