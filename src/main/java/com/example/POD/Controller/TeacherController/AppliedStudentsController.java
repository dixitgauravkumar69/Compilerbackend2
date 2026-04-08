package com.example.POD.Controller.TeacherController;


import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Service.AppliedStudentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("/api/teacher")
public class AppliedStudentsController {
    private final AppliedStudentsService appliedStudentsService;
    @GetMapping("/getAppliedStudents/{campusId}")
    public List<PlacementApplicationData>  getAppliedStudents(@PathVariable Long campusId)
    {
      return  appliedStudentsService.appliedStudent(campusId);
    }

}
