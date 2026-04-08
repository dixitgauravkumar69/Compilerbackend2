package com.example.POD.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Jobdescription implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String company;
    private String title;
    private String jobType;


    private String location;
    private Integer semester;
    private Long salaryPackage;
    private String eligibleBranch;
    private String cgpa;
    private Long bond;
    private String skillsRequired;
    private String jobDescription;
    private String selectionProcess;
    private LocalDate registrationLastDate;
    private String allocate;
}