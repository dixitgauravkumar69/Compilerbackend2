package com.example.POD.DTO;

import lombok.Data;

@Data
public class AddTestCaseDTO {
    private Long problemId;   // ProblemStatement ID
    private String inputData;    // Input for the test case
    private String expectedOutput; // Expected output
}