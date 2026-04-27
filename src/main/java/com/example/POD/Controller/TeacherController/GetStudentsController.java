package com.example.POD.Controller.TeacherController;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Service.GetStudentByTeacherService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("/api/teacher")
public class GetStudentsController {

    private final GetStudentByTeacherService getStudentByTeacherService;
    @GetMapping("/getStudents/{problemId}")
    public List<StudentsCodeReport> getStudents(@PathVariable Long problemId)
    {
        return getStudentByTeacherService.getStudents(problemId);
    }


}
