package com.example.POD.Entity;

import com.example.POD.Entity.CampusEntity;
import com.example.POD.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "placement_application_data")
public class PlacementApplicationData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "campus_id", nullable = false)
    private CampusEntity campus;


    private String applicationStatus;
    private LocalDateTime appliedDate;


    private String selectionStatus;


}
