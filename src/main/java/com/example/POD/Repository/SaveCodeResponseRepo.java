package com.example.POD.Repository;

import com.example.POD.Entity.StudentsCodeReport;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaveCodeResponseRepo extends JpaRepository<StudentsCodeReport,Long> {

    @org.springframework.cache.annotation.Cacheable(value = "problemStudentsCache", key = "#problemId")
    List<StudentsCodeReport> findByProblemId(Long problemId);
    @Modifying
    @Transactional
    void deleteByProblemId(Long id);

    Optional<StudentsCodeReport> findByUserUseridAndProblemId(Long userId, Long problemId);
    @org.springframework.cache.annotation.Cacheable(value = "studentPerformanceCache", key = "#userId")
    List<StudentsCodeReport>findByUserUserid(Long userId);

    @Query("SELECT sc FROM StudentsCodeReport sc JOIN FETCH sc.problem WHERE sc.user.userid = :userId")
    @org.springframework.cache.annotation.Cacheable(value = "studentPerformanceCache", key = "#userId")
    List<StudentsCodeReport> findByUserWithProblem(@Param("userId") Long userId);
}
