package com.example.POD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TestCase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TestCaseId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Many test cases per problem
    @JoinColumn(name = "ProblemId", nullable = false) // FK to ProblemStatement
    @JsonIgnore//for not going into loop for session
    private ProblemStatement problem;

    @Column(name = "InputData", nullable = false, columnDefinition = "TEXT")
    private String inputData;

    @Column(name = "ExpectedOutput", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}