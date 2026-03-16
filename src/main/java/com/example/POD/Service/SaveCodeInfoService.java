package com.example.POD.Service;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.SaveCodeResponseRepo;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCodeInfoService {

    private final SaveCodeResponseRepo repo;
    private final UserRepository userRepository;
    private final ProblemStatementRepo problemRepository;

    public String addStudentResponse(StudentsCodeReport studentsCodeReport,Long userId,Long problemId) {


        System.out.println(userId);
        // fetch actual entities
        UserEntity user = userRepository.findByuserid(userId);

        ProblemStatement problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // set nested entities
        studentsCodeReport.setUser(user);
        studentsCodeReport.setProblem(problem);

        // save report
        repo.save(studentsCodeReport);

        return "Information saved successfully.....";
    }
}