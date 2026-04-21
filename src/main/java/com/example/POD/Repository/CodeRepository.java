package com.example.POD.Repository;

import com.example.POD.Entity.CodeSaveEntity;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CodeRepository extends JpaRepository<CodeSaveEntity,Long> {

    @Query("SELECT c.id FROM CodeSaveEntity c WHERE c.user.userid = :userId AND c.problem.id = :problemId")
    Long findIdByUserAndProblem(@Param("userId") Long userId,
                                @Param("problemId") Long problemId);
}
