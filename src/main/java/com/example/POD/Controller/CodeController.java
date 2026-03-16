package com.example.POD.Controller;

import com.example.POD.DTO.CodeDTO;
import com.example.POD.DTO.ResponseCodeExecution;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.TestCaseRepo;
import com.example.POD.Service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "http://localhost:4200") // allow Angular app
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;
    private final ProblemStatementRepo problemStatementRepo;
    private final TestCaseRepo testcase;

    @GetMapping("/getProblem/{problemId}")
    public ProblemStatement getProblemStatement(@PathVariable Long problemId) {

        return problemStatementRepo.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found with id: " + problemId));
    }

    @GetMapping("/getTestCases/{problemId}")
    public List<TestCaseEntity> getTestcases(@PathVariable Long problemId)
    {
        return testcase.getTestCasesByProblemId(problemId);
    }


    @PostMapping("/runTestCases")
    public ResponseCodeExecution runTestCases(@RequestBody CodeDTO request) throws Exception {
        return codeService.runWithTestCases(request.getCode(),request.getLanguage(),request.getProblemId());
    }
}