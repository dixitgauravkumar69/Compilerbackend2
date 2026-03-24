package com.example.POD.Service;

import com.example.POD.Entity.CampusEntity;
import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Entity.Profile;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.CampusRepository;
import com.example.POD.Repository.PlacementApplicationRepo;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlacementApplicationService {

    private final PlacementApplicationRepo placementApplicationRepo;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CampusRepository campusRepository;

    public String PlacementdataService(Long userId, Long campusid) {

        // 🔹 USER CHECK
        UserEntity user = userRepository.findByuserid(userId);
        if (user == null) {
            return "User not found with ID: " + userId;
        }

        // 🔹 PROFILE CHECK
        Profile profile = profileRepository.findByUserUserid(userId);
        if (profile == null) {
            return "Profile not found for User ID: " + userId;
        }

        // 🔹 CAMPUS CHECK
        CampusEntity campus = campusRepository.findById(campusid)
                .orElseThrow(() -> new RuntimeException("Campus not found with ID: " + campusid));

        // 🔹 DATE CHECK
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(campus.getRegistrationLastDate())) {
            return "Application closed";
        }

        // 🔹 BASIC CGPA FILTER (optional)
        if (profile.getCgpa() < 5) {
            return "Not eligible due to low CGPA";
        }

        // 🔹 BRANCH CHECK
        if (!campus.getEligibleBranch().equalsIgnoreCase(profile.getBranch())) {
            return "Not eligible for this branch";
        }

        //IF user applied already...............
        PlacementApplicationData placement=placementApplicationRepo.findByUserUserid(userId);

        if (placement != null && "Applied".equals(placement.getApplicationStatus())) {
            return "You have applied successfully PREVIOUSLY";
        }

        Double campusCgpa = Double.parseDouble(campus.getCgpa());

        if (profile.getCgpa() >= campusCgpa &&
                profile.getSemester() >= campus.getSemester()) {

            PlacementApplicationData data = new PlacementApplicationData();
            data.setUser(user);
            data.setCampus(campus);
            data.setApplicationStatus("Applied");
            data.setAppliedDate(LocalDateTime.now());

            placementApplicationRepo.save(data);

            return "Applied successfully!";
        }


        // 🔹 FINAL FALLBACK
        return "Sorry you are not eligible contact with Placement Cell";
    }
}