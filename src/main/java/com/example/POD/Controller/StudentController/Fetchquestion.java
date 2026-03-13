package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class Fetchquestion {

    private final ProblemStatementRepo problemStatementRepo;
    @GetMapping("/getQuestions")
    public List<ProblemStatement> getQuestions()
    {
       List<ProblemStatement> mess= problemStatementRepo.findByAssignedTrue();


       return mess;
    }
}

