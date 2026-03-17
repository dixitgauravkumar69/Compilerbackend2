package com.example.POD.Controller.ProblemStatement;

import com.example.POD.DTO.ProblemStatementDTO;
import com.example.POD.Service.ProblemStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/faculty")
public class addProblemStatement {

    private final ProblemStatementService psService;
    @PostMapping("/addProblemStatement")
    public String add(@RequestBody ProblemStatementDTO problem)
    {
        String Condition=psService.addStatement(problem.getStatement(), problem.getTitle());
        return Condition;
    }

}
