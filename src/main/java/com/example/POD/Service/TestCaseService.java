package com.example.POD.Service;

import com.example.POD.Entity.TestCaseEntity;
import com.example.POD.Entity.ProblemStatement;

import com.example.POD.Repository.TestCaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}