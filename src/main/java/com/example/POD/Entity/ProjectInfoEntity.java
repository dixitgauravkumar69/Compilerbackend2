package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "projectInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Title (short heading)
    @Column(nullable = false)
    private String title;

    // Full content (main knowledge)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;



    @JdbcTypeCode(SqlTypes.ARRAY)
    private float[] embedding;
}