package com.example.POD.Repository;

import com.example.POD.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<UserEntity,Long> {

 UserEntity findByUserEmail(String email);
 UserEntity findByuserid(Long userId);
}
