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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProblemStatementRepo problemStatementRepo;
    private final ProfileRepository profileRepo;


    @Caching(evict = {
        @CacheEvict(value = "usersCache", key = "#user.userEmail"),
        @CacheEvict(value = "profileEndpointCache", allEntries = true)
    })
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
      int SEM=profileRepo.findByUserUserid(userEntity.getUserid()).getSemester();

        if (userEntity.getPassword().equals(userLoginDTO.getPassword())) {
            UserDTO userDTO = new UserDTO();

            userDTO.setUserId(String.valueOf(userEntity.getUserid()));
            userDTO.setUserName(userEntity.getUsername());
            userDTO.setUserEmail(userEntity.getUserEmail());
            userDTO.setUserRole(userEntity.getUserRole());
            userDTO.setPassword(userEntity.getPassword());
            userDTO.setSemester(SEM);
            return userDTO;
        } else {
            return null;
        }
    }


    public List<ProblemStatement> getProblemStatements() {
        return problemStatementRepo.findAll();
    }

    @CacheEvict(value = {"allProblemStatementsCache", "assignedProblemsQuery"}, allEntries = true)
    public String assignProblemStatement(Long problemId) {
        ProblemStatement problem = problemStatementRepo
                .findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        problem.setAssigned(true);

        problemStatementRepo.save(problem);

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
}