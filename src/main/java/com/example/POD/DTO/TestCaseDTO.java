package com.example.POD.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TestCaseDTO {

    private String inputData;


    private String expectedOutput;

}
