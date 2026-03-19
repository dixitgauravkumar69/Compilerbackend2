package com.example.POD.Repository;

import com.example.POD.Entity.ProblemStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProblemStatementRepo extends JpaRepository <ProblemStatement,Long>{


    Page<ProblemStatement> findByAssignedTrue(Pageable pagable);
    List<ProblemStatement>findByIsLiveTrue();


}
