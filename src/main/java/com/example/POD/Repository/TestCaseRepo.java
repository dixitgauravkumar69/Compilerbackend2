package com.example.POD.Repository;



import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.TestCaseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepo extends JpaRepository<TestCaseEntity, Long> {

//    List<TestCaseEntity> findByProblem(Long problemId);
//    List<TestCaseEntity> findByProblem(ProblemStatement problem);

    @Modifying
    @Transactional
    void deleteByProblemId(Long id);
    @Query("SELECT t FROM TestCaseEntity t WHERE t.problem.id = :problemId")
    List<TestCaseEntity> getTestCasesByProblemId(@Param("problemId") Long problemId);




}

