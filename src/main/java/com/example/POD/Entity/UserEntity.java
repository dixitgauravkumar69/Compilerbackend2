package com.example.POD.Entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter

@Entity
@Table(name = "Users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Userid")
    private Long userid;

   @Column(name="Username",nullable=false)
    private String username;

   @Column(name="Userrole",nullable = false)
    private String userRole;

   @Column(name="Useremail",nullable=false)
    private String userEmail;

   @Column(name="Password",nullable=false)
    private String password;
}