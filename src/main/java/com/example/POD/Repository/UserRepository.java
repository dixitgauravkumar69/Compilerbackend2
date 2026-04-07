package com.example.POD.Repository;

import com.example.POD.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<UserEntity,Long> {

 @org.springframework.cache.annotation.Cacheable(value = "usersCache", key = "#email", unless = "#result == null")
 @org.springframework.data.jpa.repository.Query("SELECT u FROM UserEntity u WHERE u.userEmail = :email")
 UserEntity findByUserEmail(@org.springframework.data.repository.query.Param("email") String email);
 
 @org.springframework.cache.annotation.Cacheable(value = "usersByIdCache", key = "#userId")
 @org.springframework.data.jpa.repository.Query("SELECT u FROM UserEntity u WHERE u.userid = :userId")
 UserEntity findByuserid(@org.springframework.data.repository.query.Param("userId") Long userId);
}
