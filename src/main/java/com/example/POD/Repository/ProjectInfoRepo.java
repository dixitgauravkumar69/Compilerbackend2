package com.example.POD.Repository;

import com.example.POD.Entity.Profile;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.POD.Entity.ProjectInfoEntity;

import java.util.List;

public interface ProjectInfoRepo extends JpaRepository<ProjectInfoEntity,Long> {


  @Query("SELECT s.id,s.embedding,s.title,s.content from ProjectInfoEntity s ")
    List<ProjectInfoEntity>findAllData();
}
