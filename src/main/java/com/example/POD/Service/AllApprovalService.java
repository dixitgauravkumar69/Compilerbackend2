package com.example.POD.Service;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class AllApprovalService {
    public final UserRepository userRepository;

    public List<UserEntity> approvalRequestService() {
        List<UserEntity> teachers = new ArrayList<>();

        List<UserEntity> users = userRepository.findAll();

        for (UserEntity user : users) {
            if ((user.getIsApproved() == null || Boolean.FALSE.equals(user.getIsApproved()))
                    && "TEACHER".equals(user.getUserRole())) {

                teachers.add(user);
            }
        }

        return teachers;
    }
}
