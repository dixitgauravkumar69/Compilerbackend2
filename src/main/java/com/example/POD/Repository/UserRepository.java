package com.example.POD.Repository;

import com.example.POD.Entity.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface  UserRepository extends JpaRepository<UserEntity,Long> {

 @Cacheable(value = "usersCache", key = "#email", unless = "#result == null")
 @Query("SELECT u FROM UserEntity u WHERE u.userEmail = :email")
 UserEntity findByUserEmail(@Param("email") String email);


 @Cacheable(value = "usersByIdCache", key = "#userId")
 @Query("SELECT u FROM UserEntity u WHERE u.userid = :userId")
 UserEntity findByuserid(@Param("userId") Long userId);
}
