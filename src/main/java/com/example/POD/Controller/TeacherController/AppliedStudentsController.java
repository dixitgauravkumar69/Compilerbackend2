package com.example.POD.Controller.TeacherController;


import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Service.AppliedStudentsService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/SelectionStatus/change/{campusId}/{userId}")

    public ResponseEntity changeSelectionStatus(@PathVariable Long campusId,@PathVariable Long userId,@RequestBody String currentStatus)
    {
        return appliedStudentsService.selectionStatusChange(campusId,userId,currentStatus);
    }

}
