package com.example.POD.Repository;

import com.example.POD.Entity.StudentsCodeReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaveCodeResponseRepo extends JpaRepository<StudentsCodeReport,Long> {

    List<StudentsCodeReport> findByProblemId(Long problemId);
}
