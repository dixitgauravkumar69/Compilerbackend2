package com.example.POD.DTO;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum Languages {

    JAVA("Main.java", "javac", "java Main"),
    PYTHON("main.py", null, "python3"),
    CPP("main.cpp", "g++", "./program");


    private final String fileName;
    private final String compileCommand;
    private final String runCommand;
}