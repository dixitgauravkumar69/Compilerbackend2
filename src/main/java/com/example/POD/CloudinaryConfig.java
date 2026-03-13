package com.example.POD;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;





@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dpheznvvs",
                "api_key", "533996734268175",
                "api_secret", "Rm2XK9pvecUBMvfrCXICuynlheY"
        ));
    }
}