package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/student")
public class Fetchquestion {

    private final ProblemStatementRepo problemStatementRepo;
    @GetMapping("/getQuestions")
    public Page<ProblemStatement> getQuestions(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size)
    {
        // PageRequest object create karein (Page index 0 se start hota hai)
        Pageable pageable = PageRequest.of(page, size);
       return problemStatementRepo.findByAssignedTrue(pageable);

    }
}

