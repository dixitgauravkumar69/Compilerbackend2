package com.example.POD.Repository;

import com.example.POD.Entity.Profile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
  @org.springframework.cache.annotation.Cacheable(value = "studentProfileCache", key = "#userId")
  Profile findByUserUserid(Long userId);
}
