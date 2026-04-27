package com.example.POD.Service;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeStatusService {
    private final UserRepository userRepository;

    public String changingStatus(Long userId,String status)
    {
        UserEntity user=userRepository.findByuserid(userId);

        if(user==null)
        {
            return  "User not found";
        }

        user.setStatus(status);
        userRepository.save(user);

        return "Current status for "+user.getUserid()+"is: "+user.getStatus();
    }

}
