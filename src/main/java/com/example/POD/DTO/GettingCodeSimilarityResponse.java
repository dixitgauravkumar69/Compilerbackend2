package com.example.POD.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class GettingCodeSimilarityResponse {
    private Long similarWith;
   private Integer simialrityPercentage;
   private String studentName;
   private String remark;

}
