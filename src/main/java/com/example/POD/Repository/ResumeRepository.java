package com.example.POD.Repository;

import com.example.POD.Entity.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<ResumeEntity,Long> {
    @org.springframework.cache.annotation.Cacheable(value = "resumeCache", key = "#userId")
    List<ResumeEntity> findByUserUserid(Long userId);
}
