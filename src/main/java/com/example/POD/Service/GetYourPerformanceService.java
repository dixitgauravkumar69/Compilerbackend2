package com.example.POD.Service;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Repository.SaveCodeResponseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class GetYourPerformanceService {
    private final SaveCodeResponseRepo saveCodeResponseRepo;

    public List<StudentsCodeReport> getMyPerformance(Long studentId)
    {
       return  saveCodeResponseRepo.findByUserWithProblem(studentId);
    }
}
