package com.example.POD.Service;

import com.example.POD.DTO.TestCaseDTO;
import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Entity.ProblemStatement;

import com.example.POD.Repository.TestCaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepo testCaseRepo;

    public TestCaseEntity addTestCase(ProblemStatement problem, String input, String output) {
        TestCaseEntity testCase = new TestCaseEntity();
        testCase.setProblem(problem);
        testCase.setInputData(input);
        testCase.setExpectedOutput(output);
        return testCaseRepo.save(testCase);
    }

//    public List<TestCaseEntity> getTestCasesForProblem(ProblemStatement problem) {
//        return testCaseRepo.findByProblem(problem);
//    }



    public List<TestCaseEntity> getTestCase(Long pId)
    {
       return  testCaseRepo.getTestCasesByProblemId(pId);
    }


    public ResponseEntity<?> editTestCase(Long pId, TestCaseDTO testCaseDTO) {
        List<TestCaseEntity> testCases = testCaseRepo.getTestCasesByProblemId(pId);

        if (testCases == null || testCases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No test cases found to edit for problem id: " + pId);
        }

        // Example: Update all test cases with same data
        testCases.forEach(tc -> {
            tc.setInputData(testCaseDTO.getInputData());
            tc.setExpectedOutput(testCaseDTO.getExpectedOutput());
        });

        testCaseRepo.saveAll(testCases);

        // Return a response with success message and updated count
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Test cases updated successfully.");
        response.put("updatedCount", testCases.size());
        response.put("updatedTestCases", testCases);

        return ResponseEntity.ok(response);
    }


    public String deleteTestCase(Long tId)
    {
        Optional<TestCaseEntity> testcase= testCaseRepo.findById(tId);

        if (testcase==null) {
           return "Not found any Testcases for this Problem";
        }

        else
        {
            testCaseRepo.deleteById(tId);

        }
        return" Test Case Deleted successfully"+ tId;
    }

}