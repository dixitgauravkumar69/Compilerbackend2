package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Service.EmailService;
import com.example.POD.Service.SaveCodeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class SaveCodeData {


    private final SaveCodeInfoService saveCodeInfoService;
    private final EmailService emailService;

    @PostMapping("/SaveStudentCodeInfo/{userId}/{problemId}")
    public String SaveStudentProblemSolution(
            @RequestBody StudentsCodeReport studentsCodeReport,
            @PathVariable Long userId,
            @PathVariable Long problemId)
    {
        // Pehle data database mein save karein
        String response = saveCodeInfoService.addStudentResponse(studentsCodeReport, userId, problemId);

        // Yahan hum studentsCodeReport se data nikal rahe hain
        if (response != null && !response.contains("Error")) {
            emailService.sendStudentPerformanceReport(
                    studentsCodeReport.getUser().getUserEmail(),
                    studentsCodeReport.getUser().getUsername(),
                    problemId,
                    studentsCodeReport.getMarks(),
                    studentsCodeReport.getTakenTime()
            );
        }

        return response;
    }
}


