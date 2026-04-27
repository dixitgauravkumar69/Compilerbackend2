package com.example.POD.Service;

import com.example.POD.DTO.UsersForAdminDTO;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllUsersService {
    private final UserRepository userRepository;
    public List<UsersForAdminDTO> allUsers()
    {
        List<UsersForAdminDTO>Allusers=new ArrayList<>();// for sending to admin

        List<UserEntity>users=userRepository.findAll();//for fetching from api


        for(UserEntity user:users)// Traversing and fetch one-one user and add them in list
        {
            UsersForAdminDTO signleUser=new UsersForAdminDTO();

            signleUser.setUserEmail(user.getUserEmail());
            signleUser.setUserRole(user.getUserRole());
            signleUser.setUsername(user.getUsername());
            signleUser.setStatus(user.getStatus());
            signleUser.setUserId(user.getUserid());
            Allusers.add(signleUser);
        }

        return Allusers;
    }
}
