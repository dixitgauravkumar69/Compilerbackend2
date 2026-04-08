package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "resume")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResumeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String college;
    private String branch;
    private Double cgpa;

    @Column(nullable = false)
    private String skills;

    @Column(nullable=false)
    private String github;

    @Column(nullable = false)
    private String linkedin;

    @Column(nullable=false,columnDefinition = "TEXT")
    private String projects;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String internships;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String achievements;
    @Column(nullable=false)
    private String bio;

    @Column(nullable=false,columnDefinition = "TEXT")
    private String summary;


    private String highSchool;


    private String highSchoolMarks;


    private String higherSecondary;


    private String higherSecondaryMarks;
    private String semester; // futuristic not working yet ......

    private String img;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}