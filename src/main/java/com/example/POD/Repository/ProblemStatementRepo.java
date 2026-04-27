package com.example.POD.Repository;

import com.example.POD.DTO.ProblemStatementListDTO;
import com.example.POD.Entity.ProblemStatement;
import org.springframework.data.domain.Page;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProblemStatementRepo extends JpaRepository <ProblemStatement,Long>{


    @Override
    List<ProblemStatement> findAll();

    Page<ProblemStatement> findByAssignedTrue(Pageable pagable);
    List<ProblemStatement>findByIsLiveTrueAndSemester(Integer semester);


    List<ProblemStatement>findByIsLiveTrue();



    @Query("SELECT p FROM ProblemStatement p WHERE p.isLive = false AND p.startTime <= CURRENT_TIMESTAMP")
    List<ProblemStatement> findProblemsToActivate();

    @Query("SELECT p FROM ProblemStatement p WHERE p.isLive = true AND p.endTime <= CURRENT_TIMESTAMP")
    List<ProblemStatement> findProblemsToDeactivate();

    @Cacheable(value = "assignedProblemsQuery", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Query("SELECT p FROM ProblemStatement p WHERE p.assigned = true ")
    Page<ProblemStatement> findActiveAssignedProblems(Pageable pageable);

    @Query("SELECT p FROM ProblemStatement p WHERE p.assigned = true ")
    org.springframework.data.domain.Slice<ProblemStatementListDTO> findActiveAssignedProblemsOptimized(Pageable pageable);
}

