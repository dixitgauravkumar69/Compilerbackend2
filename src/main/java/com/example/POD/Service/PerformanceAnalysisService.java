package com.example.POD.Service;

import com.example.POD.DTO.PerformanceAnalysisDTO;
import com.example.POD.Repository.SaveCodeResponseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PerformanceAnalysisService {
    private final SaveCodeResponseRepo analysisRepo;

    public PerformanceAnalysisDTO getAnaliticsOfStudents(Long problemId)
    {
        PerformanceAnalysisDTO performanceAnalysis=new PerformanceAnalysisDTO();

       performanceAnalysis.setCountOfSubmitionOnSemester(analysisRepo.countBySemester(problemId));
        performanceAnalysis.setCountOfSubmitionOnReason(analysisRepo.countBySubmissionReason(problemId));



        return performanceAnalysis;
    }

}
