package com.example.POD.Entity;


import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="ActualCode")
@Getter
@Setter


public class CodeSaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String code;

   private List<String> similarity;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name="ProblemId")
    private ProblemStatement problem;


}
