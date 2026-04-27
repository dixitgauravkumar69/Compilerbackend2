package com.example.POD.Entity;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

   @Column(name="isApproved")
    private Boolean isApproved;

    @Column(name="status")
    private String status = "ACTIVE";
}