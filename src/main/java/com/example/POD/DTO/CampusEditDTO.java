package com.example.POD.DTO;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter

public class CampusEditDTO {
    private String title;
    private String company;
    private String jobType;
    private String location;
    private Integer semester;
    private String eligibleBranch;
    private  String cgpa;
    private Long salaryPackage;
    private Long bond;
    private String skillsRequired;
    private String jobDescription;
    private String selectionProcess;
    private LocalDate registrationLastDate;


}
