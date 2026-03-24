package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "placement_application_data")
public class PlacementApplicationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "campus_id", nullable = false)
    private CampusEntity campus;


    private String applicationStatus;
    private LocalDateTime appliedDate;


}
