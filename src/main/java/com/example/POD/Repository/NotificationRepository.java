package com.example.POD.Repository;

import com.example.POD.Entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Long> {

    List<NotificationEntity> findByUserId(Long userId);
}
