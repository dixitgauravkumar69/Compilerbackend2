package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ResumeEntity {

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

    @Column(nullable=false)
    private String projects;

    @Column(nullable = false)
    private String internships;

    @Column(nullable = false)
    private String achievements;
    @Column(nullable=false)
    private String bio;

    @Column(nullable=false)
    private String summary;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}