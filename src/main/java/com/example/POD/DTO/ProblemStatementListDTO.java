package com.example.POD.DTO;

import java.time.LocalDateTime;

public interface ProblemStatementListDTO {
    Long getId();
    String getTitle();
    Boolean getAssigned();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    String getLevel();
    Boolean getIsLive();
    Integer getSemester();
}
