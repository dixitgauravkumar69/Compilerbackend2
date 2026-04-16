package com.example.POD.Service;

import com.example.POD.Controller.NotificationController;
import com.example.POD.DTO.UserDTO;
import com.example.POD.DTO.UserLoginDTO;
import com.example.POD.Entity.NotificationEntity;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.Profile;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.NotificationRepository;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ProblemStatementRepo problemStatementRepo;
    private final ProfileRepository profileRepo;
    private final CacheManager cacheManager;
    private final NotificationController notificationController;
    private final NotificationRepository notificationRepository;


    @Caching(evict = {
        @CacheEvict(value = "usersCache", key = "#user.userEmail"),
        @CacheEvict(value = "profileEndpointCache", allEntries = true)
    })
    public UserEntity addUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();


        userEntity.setUsername(user.getUserName());
        userEntity.setUserRole(user.getUserRole());
        userEntity.setUserEmail(user.getUserEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        

        userRepository.save(userEntity);

        return userEntity;
    }

    public UserDTO loginUser(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userRepository.findByUserEmail(userLoginDTO.getUserEmail());

        if (userEntity == null) {
            return null;
        }

        if (userEntity.getPassword() == null || userEntity.getPassword().trim().isEmpty()) {
            return null;
        }

        if (passwordEncoder.matches(userLoginDTO.getPassword(), userEntity.getPassword())) {
            int SEM = profileRepo.findByUserUserid(userEntity.getUserid()).getSemester();
            UserDTO userDTO = new UserDTO();

            userDTO.setUserId(String.valueOf(userEntity.getUserid()));
            userDTO.setUserName(userEntity.getUsername());
            userDTO.setUserEmail(userEntity.getUserEmail());
            userDTO.setUserRole(userEntity.getUserRole());
            userDTO.setPassword(null); // Do not send password back to client
            userDTO.setSemester(SEM);
            return userDTO;
        } else {
            return null;
        }
    }


    @PreAuthorize("hasRole('TEACHER')")
    public List<ProblemStatement> getProblemStatements() {
        return problemStatementRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery"}, allEntries = true)
    public String assignProblemStatement(Long problemId) {
        ProblemStatement problem = problemStatementRepo
                .findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        problem.setAssigned(true);

        problemStatementRepo.save(problem);


// Sending real time notification and save it .................................for while

        List<Long>users;

        if(problem.getSemester()==null)
        {
            users=profileRepo.findAllUsers();
            System.out.println(users);
        }

        else {
            users= profileRepo.findBySemester(problem.getSemester());
            System.out.println(users);

        }

        for(Long user:users)
        {

            NotificationEntity notificationEntity=new NotificationEntity();
            //send in real time
            notificationController.sendToUser(user,"New problem for practice is added ");

            //Saved in db
            notificationEntity.setUserId(user);
            notificationEntity.setMessage("New problem for practice is added");
            notificationEntity.setType("addedProblem");


            notificationRepository.save(notificationEntity);
        }

        return "Assigned successfully";
    }

    @CacheEvict(value = "studentProfileCache", key = "#userId")
    public ResponseEntity<?> addStudentProfile(Profile profile,Long userId)
    {

        UserEntity user = userRepository.findByuserid(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        profile.setUser(user);

        Profile savedProfile = profileRepo.save(profile);

        return ResponseEntity.ok(savedProfile);
    }

    public ResponseEntity<?> ChangePassword(String email, String oldPassword, String updatedPassword) {

        UserEntity user = userRepository.findByUserEmail(email);

        //  Check user exists
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        //  Check old password match
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid old password");
        }

        //  Check new password not same as old
        if (oldPassword.equals(updatedPassword)) {
            return ResponseEntity.status(400).body("New password cannot be same as old password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(updatedPassword));
        userRepository.save(user);

        // Manual Cache Eviction to ensure data consistency
        if (cacheManager.getCache("usersCache") != null) {
            cacheManager.getCache("usersCache").evict(email);
        }
        if (cacheManager.getCache("usersByIdCache") != null) {
            cacheManager.getCache("usersByIdCache").evict(user.getUserid());
        }
        if (cacheManager.getCache("studentProfileCache") != null) {
            cacheManager.getCache("studentProfileCache").evict(user.getUserid());
        }

        return ResponseEntity.status(200).body("Password updated successfully");
    }
}