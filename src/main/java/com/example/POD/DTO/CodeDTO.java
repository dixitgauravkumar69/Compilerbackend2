package com.example.POD.DTO;

import com.example.POD.DTO.Languages;
import lombok.RequiredArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CodeDTO {
    private String code;
    private Languages language;
    private Long problemId;
//    private String input;
}
