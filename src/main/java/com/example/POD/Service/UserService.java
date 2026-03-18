package com.example.POD.Service;

import com.example.POD.DTO.UserDTO;
import com.example.POD.DTO.UserLoginDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.Profile;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProblemStatementRepo problemStatementRepo;
    private final ProfileRepository profileRepo;

    public UserEntity addUser(UserDTO user) {
        UserEntity userEntity = new UserEntity();


        userEntity.setUsername(user.getUserName());
        userEntity.setUserRole(user.getUserRole());
        userEntity.setUserEmail(user.getUserEmail());
        userEntity.setPassword(user.getPassword());


        userRepository.save(userEntity);

        return userEntity;
    }

    public UserDTO loginUser(UserLoginDTO userLoginDTO) {
        UserEntity userEntity = userRepository.findByUserEmail(userLoginDTO.getUserEmail());

        if (userEntity == null) {
            return null;
        }

        if (userEntity.getPassword().equals(userLoginDTO.getPassword())) {
            UserDTO userDTO = new UserDTO();

            userDTO.setUserId(String.valueOf(userEntity.getUserid()));
            userDTO.setUserName(userEntity.getUsername());
            userDTO.setUserEmail(userEntity.getUserEmail());
            userDTO.setUserRole(userEntity.getUserRole());
            userDTO.setPassword(userEntity.getPassword());

            return userDTO;
        } else {
            return null;
        }
    }


    public List<ProblemStatement> getProblemStatements() {
        return problemStatementRepo.findAll();
    }

    public String assignProblemStatement(Long problemId) {
        ProblemStatement problem = problemStatementRepo
                .findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        problem.setAssigned(true);

        problemStatementRepo.save(problem);

        return "Assigned successfully";
    }

    public ResponseEntity<?> addStudentProfile(Profile profile,Long userId)
    {



        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        profile.setUser(user);

        Profile savedProfile = profileRepo.save(profile);

        return ResponseEntity.ok(savedProfile);
    }
}