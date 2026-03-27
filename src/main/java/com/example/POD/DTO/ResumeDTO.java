package com.example.POD.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeDTO {


    private String projects;
    private String internships;
    private String bio;
    private String semester;
    private String summary;
    private String achievements;
    private String highSchool;
    private String highSchoolMarks;
    private String higherSecondary;
    private String higherSecondaryMarks;
    private String img;
}
