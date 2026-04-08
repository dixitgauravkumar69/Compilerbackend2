package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ProblemStatement")
public class ProblemStatement implements Serializable {

    private static final long serialVersionUID = 1L;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ProblemId")
    private Long id;

    @Column(name = "Title")
    private String title;

    @Column(name = "ProblemStatement", nullable = false)
    private String problemStatement;

    @Column(name="Assigned")
    private Boolean assigned;

    @Column(name="StartTime")
    private LocalDateTime startTime;

    @Column(name="EndTime")
    private LocalDateTime endTime;

    @Column(name="Level")
    private String level;

    @Column(name="IsLive")
    private Boolean isLive;

    @Column(name="Semester")
    private Integer semester;
}