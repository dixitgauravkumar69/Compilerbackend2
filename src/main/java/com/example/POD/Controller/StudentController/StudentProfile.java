package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.Profile;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentProfile {
    private final UserService userService;
    private final ProfileRepository profileRepository;
    @PostMapping("/addProfile/{userId}")
    public ResponseEntity<?> addProfile(@RequestBody Profile profile, @PathVariable Long userId)
    {
        ResponseEntity<?> StudentProfile=userService.addStudentProfile(profile,userId);
        return StudentProfile;
    }

    @GetMapping("Profile/{userId}")
    public Profile getProfile(@PathVariable Long userId)
    {
        return profileRepository.findByUserUserid(userId);
    }
}
