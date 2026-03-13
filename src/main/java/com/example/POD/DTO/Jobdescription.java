package com.example.POD.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor

public class Jobdescription {
    private Long id;

    private String company;

    private String title;

    private String jobType;

    private String industry;

    private String location;

    private Integer semester;

    private String salaryPackage;

    private String eligibility;

    private String bond;

    private String skillsRequired;

    private String jobDescription;

    private String selectionProcess;

    private LocalDate registrationLastDate;

   private String allocate;
}
