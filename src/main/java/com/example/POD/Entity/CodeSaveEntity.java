package com.example.POD.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ActualCode")
@Getter
@Setter


public class CodeSaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String code;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name="ProblemId")
    private ProblemStatement problem;


}
