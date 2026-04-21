package com.example.POD.Service;

import com.example.POD.Entity.CodeSaveEntity;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.StudentsCodeReport;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.CodeRepository;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.SaveCodeResponseRepo;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveCodeInfoService {

    private final SaveCodeResponseRepo repo;
    private final UserRepository userRepository;
    private final ProblemStatementRepo problemRepository;
    private final CodeRepository codeRepository;

    public String addStudentResponse(StudentsCodeReport studentsCodeReport, Long userId, Long problemId) {

        // 1. Check if the report already exists
        Optional<StudentsCodeReport> existingReport = repo.findByUserUseridAndProblemId(userId, problemId);

        if (existingReport.isPresent()) {
            //  Purani ID naye report object mein set kr li
            studentsCodeReport.setId(existingReport.get().getId());
            System.out.println("Updating existing report for User: " + userId);
        } else {
            System.out.println("Creating new report for User: " + userId);
        }

        // 2. Fetch actual entities
        UserEntity user = userRepository.findByuserid(userId);
        ProblemStatement problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // 3. Set nested entities
        studentsCodeReport.setUser(user);
        studentsCodeReport.setProblem(problem);

        // 4. Save (If ID exists, it updates. If ID is null, it inserts.)
        repo.save(studentsCodeReport);


        int tmpM=studentsCodeReport.getMarks();// debugging line ..............



        return existingReport.isPresent() ? "Response updated successfully!" : "Information saved successfully!"+ tmpM;
    }


    public ResponseEntity<CodeSaveEntity> savingActualCode(Long userId, Long problemId, String code)
    {
        CodeSaveEntity codeData=new CodeSaveEntity();

        Long alreadyId=codeRepository.findIdByUserAndProblem(userId, problemId);
        if(alreadyId!=null)
        {
            codeData.setId(alreadyId);
        }

        UserEntity user=userRepository.findByuserid(userId);
        if(user!=null)
        {
            codeData.setUser(user);
        }

        ProblemStatement problemStatement =
                problemRepository.findById(problemId)
                        .orElseThrow(() -> new RuntimeException("Problem not found"));

        if(problemStatement!=null)
        {
            codeData.setProblem(problemStatement);
        }

        codeData.setCode(code);

       CodeSaveEntity savedCode= codeRepository.save(codeData);

        return ResponseEntity.ok(savedCode);

    }

}