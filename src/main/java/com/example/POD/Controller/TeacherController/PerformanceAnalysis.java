package com.example.POD.Controller.TeacherController;

import com.example.POD.DTO.PerformanceAnalysisDTO;
import com.example.POD.Service.PerformanceAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")

@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequiredArgsConstructor
public class PerformanceAnalysis {
    private final PerformanceAnalysisService performanceAnalysisService;
    @GetMapping("/getStudents/PerformanceAnalysis/{problemId}")
    public PerformanceAnalysisDTO studentPerformaceAnalysis(@PathVariable Long problemId)
    {
        return performanceAnalysisService.getAnaliticsOfStudents(problemId);
    }
}
