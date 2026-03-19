package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter

@Entity
@Table(name = "ProblemStatement")
public class ProblemStatement {

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

    @Column(name="IsLive")
    private Boolean isLive;
}