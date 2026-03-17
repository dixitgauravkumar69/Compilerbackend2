package com.example.POD.Controller.TeacherController;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Service.GetStudentByTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher")
public class GetStudentsController {

    private final GetStudentByTeacherService getStudentByTeacherService;
    @GetMapping("/getStudents/{problemId}")
    public List<StudentsCodeReport> getStudents(@PathVariable Long problemId)
    {
        return getStudentByTeacherService.getStudents(problemId);
    }
}
