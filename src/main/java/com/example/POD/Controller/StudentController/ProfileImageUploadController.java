package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.Profile;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Service.UploadImageService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class ProfileImageUploadController {

    private final UploadImageService imageService;
    private final ProfileRepository profileRepository;
    @PostMapping("/uploadImg/{userId}")
    @CacheEvict(value = "studentProfileCache", key = "#userId")
    public ResponseEntity<?>uploadImage(@PathVariable Long userId, @RequestParam("file")MultipartFile file)
    {
        try {
            // 1. Image upload to Cloudinary
            String imageUrl = imageService.uploadImage(file);

            System.out.println(imageUrl);
            // 2. Database mein URL save karein
            Profile profile = profileRepository.findByUserUserid(userId);


            profile.setImg(imageUrl);
            profileRepository.save(profile);

            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }
}
