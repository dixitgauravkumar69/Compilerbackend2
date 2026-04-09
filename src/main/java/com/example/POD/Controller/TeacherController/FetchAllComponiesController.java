package com.example.POD.Controller.TeacherController;

import com.example.POD.DTO.CampusEditDTO;
import com.example.POD.Entity.CampusEntity;
import com.example.POD.Service.EditCampusService;
import com.example.POD.Service.FetchComponiesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Getter
@Setter
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("/api/teacher")
public class FetchAllComponiesController {
    private final FetchComponiesService fetchComponiesService;
    private final EditCampusService editCampusService;


    @GetMapping("/getAllComponies/{teacherId}")
    public List<CampusEntity> getComponiesInformation(@PathVariable Long teacherId)
    {
        return fetchComponiesService.getAllCompony(teacherId);
    }


    @PatchMapping("/placement/updateJob/{CompanyId}")
    public String updateJob(@PathVariable Long CompanyId, @RequestBody CampusEditDTO editedCampusInfo)
    {
       return editCampusService.editCampus(editedCampusInfo,CompanyId);
    }
}
