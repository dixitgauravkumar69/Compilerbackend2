package com.example.POD.Service;

import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Repository.SaveCodeResponseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetStudentPerformanceService {
    private final SaveCodeResponseRepo saveCodeResponseRepo;

    public List<StudentsCodeReport>StudentReport(Long studentId)
    {
        return  saveCodeResponseRepo.findByUserWithProblem(studentId);
    }

}
