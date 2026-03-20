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
public class GetLiveStreamTest {
    private final ProblemStatementRepo problemStatementRepo;
    @GetMapping("/getLiveStream")
    public List<ProblemStatement> getLiveProblems()
    {
        return problemStatementRepo.findByIsLiveTrue();
    }
}
