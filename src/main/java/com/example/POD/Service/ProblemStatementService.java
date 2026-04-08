package com.example.POD.Service;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ProblemStatementService {

    private final ProblemStatementRepo psRepo;

    @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery"}, allEntries = true)
    public String addStatement(String Statement,String title,String level)
    {
        ProblemStatement psEntity=new ProblemStatement();
        psEntity.setProblemStatement(Statement);
        psEntity.setAssigned(false);
        psEntity.setTitle(title);
        psEntity.setLevel(level);
        psRepo.save(psEntity);

        return "Problem saved with ID:"+ psEntity.getId();
    }
}
