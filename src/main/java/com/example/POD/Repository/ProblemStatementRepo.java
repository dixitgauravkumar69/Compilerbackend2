package com.example.POD.Repository;

import com.example.POD.Entity.ProblemStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemStatementRepo extends JpaRepository <ProblemStatement,Long>{


    List<ProblemStatement> findByAssignedTrue();

}
