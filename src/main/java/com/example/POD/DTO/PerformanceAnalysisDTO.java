package com.example.POD.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PerformanceAnalysisDTO {
    private List<Object[]> countOfSubmitionOnSemester;
    private List<Object[]>  countOfSubmitionOnReason;
}
