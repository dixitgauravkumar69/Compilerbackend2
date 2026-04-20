package com.example.POD.Repository;

import com.example.POD.Entity.StudentsCodeReport;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaveCodeResponseRepo extends JpaRepository<StudentsCodeReport,Long> {

    @Cacheable(value = "problemStudentsCache", key = "#problemId")
    List<StudentsCodeReport> findByProblemId(Long problemId);
    @Modifying
    @Transactional
    void deleteByProblemId(Long id);

    @Query("SELECT sc FROM StudentsCodeReport sc " +
            "JOIN FETCH sc.user u " +
            "JOIN FETCH sc.problem p " +
            "WHERE u.userid = :userId AND p.id = :problemId")
    Optional<StudentsCodeReport> findByUserUseridAndProblemId(
            @Param("userId") Long userId,
            @Param("problemId") Long problemId
    );

//    @Cacheable(value = "studentPerformanceCache", key = "#userId")
//    List<StudentsCodeReport>findByUserUserid(Long userId);


    @Cacheable(value = "studentPerformanceCache", key = "#userId")
    @Query("SELECT sc FROM StudentsCodeReport sc JOIN FETCH sc.problem WHERE sc.user.userid = :userId")
    List<StudentsCodeReport> findByUserWithProblem(@Param("userId") Long userId);



    // 1. Group by Semester: How many students from each sem solved a specific problem
    @Query("SELECT p.semester, COUNT(s) FROM StudentsCodeReport s " +
            "JOIN Profile p ON s.user.id = p.user.id " +
            "WHERE s.problem.id = :problemId " +
            "GROUP BY p.semester")
    List<Object[]> countBySemester(@Param("problemId") Long problemId);

    // 2. Group by Abnormal Reason: To analyze cheating/behavior patterns
    @Query("SELECT s.abnormalSubmitionReason, COUNT(s) FROM StudentsCodeReport s " +
            "WHERE s.problem.id = :problemId " +
            "GROUP BY s.abnormalSubmitionReason")
    List<Object[]> countBySubmissionReason(@Param("problemId") Long problemId);
}
