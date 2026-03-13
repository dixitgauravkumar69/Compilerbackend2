package com.example.POD.Service;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ProblemStatementService {

    private final ProblemStatementRepo psRepo;

    public String addStatement(String Statement,String title)
    {
        ProblemStatement psEntity=new ProblemStatement();
        psEntity.setProblemStatement(Statement);
        psEntity.setAssigned(false);
        psEntity.setTitle(title);
        psRepo.save(psEntity);

        return "Problem saved with ID:"+ psEntity.getId();
    }
}
