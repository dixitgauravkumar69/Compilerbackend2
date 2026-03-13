package com.example.POD.Repository;

import com.example.POD.Entity.Profile;
import com.example.POD.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
  Profile findByUserUserid(Long userId);
}
