package com.example.POD.Repository;

import com.example.POD.Entity.ResumeEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<ResumeEntity,Long> {
    @Cacheable(value = "resumeCache", key = "#userId")
    List<ResumeEntity> findByUserUserid(Long userId);
}
