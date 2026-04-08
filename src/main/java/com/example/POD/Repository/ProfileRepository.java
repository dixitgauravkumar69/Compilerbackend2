package com.example.POD.Repository;

import com.example.POD.Entity.Profile;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
  @Cacheable(value = "studentProfileCache", key = "#userId")
  Profile findByUserUserid(Long userId);
}
