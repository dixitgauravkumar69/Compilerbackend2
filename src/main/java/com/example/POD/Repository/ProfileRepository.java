package com.example.POD.Repository;

import com.example.POD.Entity.Profile;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
  @Cacheable(value = "studentProfileCache", key = "#userId")
  Profile findByUserUserid(Long userId);


  @Query("SELECT p.user.userid FROM Profile p WHERE p.branch = :branch AND p.semester = :semester")
  List<Long> findByBranchAndSemester(@Param("branch") String branch,
                                     @Param("semester") Integer semester);

  @Query("Select p.user.userid from Profile p WHERE p.semester=:semester")
  List<Long>findBySemester(@Param("semester")Integer semester);

  @Query("Select p.user.userid from Profile p")
  List<Long>findAllUsers();


}
