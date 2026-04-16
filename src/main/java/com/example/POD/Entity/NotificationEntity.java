package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Table(name="Notification")


public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long userId;


    private String message;
    private String type; // type of notification age jb frontend me click krne me yha vha jana padega

    //Notification is read or not ...

    private boolean isRead=false;

    private LocalDateTime createdAt; // for showing time of adding notification

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
