package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Repository.SaveCodeResponseRepo;
import com.example.POD.Service.GetYourPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class GetYourPerformance {
    private final GetYourPerformanceService getYourPerformanceService;

    @GetMapping("/getYourPerformance/{studentId}")
    public List<StudentsCodeReport> getYourPerformance(@PathVariable Long studentId)
    {
       return getYourPerformanceService.getMyPerformance(studentId);
    }
}
