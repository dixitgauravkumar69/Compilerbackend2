package com.example.POD.DTO;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCodeExecution {
   private  List<String>testCases;
   private Integer marks;
}
