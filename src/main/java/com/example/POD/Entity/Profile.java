package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String phone;

    private String college;

    private String branch;

    private Integer semester;

    private Double cgpa;

    private String skills;

    private String github;

    private String linkedin;

//    private Integer problemsSolved;

    private String bio;

    // Join with User Table
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}