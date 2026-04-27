package com.example.POD.Service;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UpdateApprovalService {
    private final UserRepository userRepository;

    public String updateApproval(Long userId,Boolean status)
    {
        UserEntity user=userRepository.findByuserid(userId);

        if(user==null)
        {
            return "Ensure your userId";
        }

        user.setIsApproved(status);
        userRepository.save(user);
        return "Your request is :"+user.getIsApproved();
    }
}
