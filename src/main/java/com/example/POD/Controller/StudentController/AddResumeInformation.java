package com.example.POD.Controller.StudentController;

import com.example.POD.DTO.ResumeDTO;
import com.example.POD.Entity.ResumeEntity;
import com.example.POD.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class AddResumeInformation {

    private final ResumeService resumeService;

    @PostMapping("/addResumeInfo/{userId}")
    public ResumeEntity addResumeInfo(@RequestBody ResumeDTO resumeInfo, @PathVariable Long userId)
    {
        return resumeService.addResumeInfo(resumeInfo,userId);
    }
}
