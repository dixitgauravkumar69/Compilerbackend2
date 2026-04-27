package com.example.POD.Service;

import com.example.POD.Entity.CampusEntity;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.CampusRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchComponiesService {
    private final CampusRepository campusRepository;
    private final UserRepository user;




    public List<CampusEntity> getAllCompony(Long teacherId)
    {

        UserEntity Teacher=user.findByuserid(teacherId);

        if(Teacher!=null)
        {
            List<CampusEntity> campus= campusRepository.findAll();
            System.out.println("Campus cached");
            return campus;
        }
        return java.util.Collections.emptyList();

    }
}
