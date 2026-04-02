package com.example.POD.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CampusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String company;

    private String jobType;



    private String location;

    private Integer semester;



    private String eligibleBranch;

    private  String cgpa;


    private Long salaryPackage;
    private Long bond;



    @Column(length = 2000)
    private String skillsRequired;

    @Column(length = 5000)
    private String jobDescription;

    @Column(length = 2000)
    private String selectionProcess;

    private LocalDate registrationLastDate;

    private LocalDateTime createdAt;

    private String attachment;

}