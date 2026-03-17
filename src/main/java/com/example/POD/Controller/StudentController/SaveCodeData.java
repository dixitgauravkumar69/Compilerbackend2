package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Service.SaveCodeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class SaveCodeData {


    private final SaveCodeInfoService saveCodeInfoService;


    @PostMapping("/SaveStudentCodeInfo/{userId}/{problemId}")
    public String SaveStudentProblemSolution(@RequestBody StudentsCodeReport studentsCodeReport, @PathVariable Long userId, @PathVariable Long problemId)
    {
       return  saveCodeInfoService.addStudentResponse(studentsCodeReport,userId,problemId);
    }


}
