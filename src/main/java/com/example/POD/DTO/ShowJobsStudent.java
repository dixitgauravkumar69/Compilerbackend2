package com.example.POD.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShowJobsStudent {
    private Long id;

    private String company;

    private String title;

    private String jobType;

    private String industry;

    private String location;

    private LocalDate registrationLastDate;
    private String attachment;
}
