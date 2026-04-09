package com.example.POD.Service;

import com.example.POD.DTO.CampusEditDTO;
import com.example.POD.Entity.CampusEntity;
import com.example.POD.Repository.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EditCampusService {
    private final CampusRepository campusRepository;



    @CacheEvict(value = "allCompaniesCache", key = "'allCompany'")
    public String editCampus(CampusEditDTO campusEditedInfo, Long campusID) {

        //Campus phle find krta hu via campusID
        CampusEntity campus = campusRepository.findById(campusID)
                .orElseThrow(() -> new RuntimeException("Campus not found with ID: " + campusID));

        // Update fields
        campus.setTitle(campusEditedInfo.getTitle());
        campus.setCompany(campusEditedInfo.getCompany());
        campus.setJobType(campusEditedInfo.getJobType());
        campus.setLocation(campusEditedInfo.getLocation());
        campus.setSemester(campusEditedInfo.getSemester());
        campus.setEligibleBranch(campusEditedInfo.getEligibleBranch());
        campus.setCgpa(campusEditedInfo.getCgpa());
        campus.setSalaryPackage(campusEditedInfo.getSalaryPackage());
        campus.setBond(campusEditedInfo.getBond());
        campus.setSkillsRequired(campusEditedInfo.getSkillsRequired());
        campus.setJobDescription(campusEditedInfo.getJobDescription());
        campus.setSelectionProcess(campusEditedInfo.getSelectionProcess());
        campus.setRegistrationLastDate(campusEditedInfo.getRegistrationLastDate());

        // Save updated entity
        campusRepository.save(campus);
        System.out.println("Cache Cleared ");

        return "Campus updated successfully ";
    }
}
