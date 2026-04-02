package com.example.POD.Controller.TestCases;

import com.example.POD.DTO.AddTestCaseDTO;
import com.example.POD.DTO.TestCaseDTO;
import com.example.POD.Entity.ProblemStatement;

import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Service.TestCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcase")
@RequiredArgsConstructor
public class addTestCase {

    private final TestCaseService testCaseService;
    private final ProblemStatementRepo problemRepo;

    @PostMapping("/addTestCase")
    public String addTestCases(@RequestBody AddTestCaseDTO dto) {

        // 1️ Find the problem by ID
        ProblemStatement problem = problemRepo.findById(dto.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found with ID: " + dto.getProblemId()));

        // 2️ Save test case
        TestCaseEntity testCase = testCaseService.addTestCase(
                problem,
                dto.getInputData(),
                dto.getExpectedOutput()
        );

        return "Test case added with ID: " + testCase.getId();
    }



    @GetMapping("/getTestCase/{pId}")
    public List<TestCaseEntity> getTestCases(@PathVariable Long pId)
    {
        return testCaseService.getTestCase(pId);
    }

    @PutMapping("/editTestCase/{pId}")
    public ResponseEntity<?> editTestCase(@PathVariable Long pId, @RequestBody TestCaseDTO testCaseDTO)

    {
        return testCaseService.editTestCase(pId,testCaseDTO);
    }


    @DeleteMapping("/deleteTestCase/{tId}")
        public String deleteTestCase(@PathVariable Long tId)
        {
            return testCaseService.deleteTestCase(tId);
        }
}