package com.example.POD.Repository;

import com.example.POD.Entity.StudentsCodeReport;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface SaveCodeResponseRepo extends JpaRepository<StudentsCodeReport,Long> {

    List<StudentsCodeReport> findByProblemId(Long problemId);
    @Modifying
    @Transactional
    void deleteByProblemId(Long id);

    Optional<StudentsCodeReport> findByUserUseridAndProblemId(Long userId, Long problemId);
    List<StudentsCodeReport>findByUserUserid(Long userId);
}
