package com.example.POD.Repository;

import com.example.POD.Entity.CodeSaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeRepository extends JpaRepository<CodeSaveEntity,Long> {

    @Query("SELECT c FROM CodeSaveEntity c WHERE c.user.userid = :userId AND c.problem.id = :problemId")
    CodeSaveEntity findByUserAndProblem(@Param("userId") Long userId,
                                        @Param("problemId") Long problemId);


   List<CodeSaveEntity> findByProblemId(Long problemId);
}
