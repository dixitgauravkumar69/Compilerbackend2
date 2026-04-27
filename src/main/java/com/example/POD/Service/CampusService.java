package com.example.POD.Service;

import com.example.POD.Entity.CampusEntity;
import com.example.POD.Repository.CampusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CampusService {
    private final CampusRepository campusRepository;

    public CampusEntity addJob(CampusEntity campus)
    {
        campus.setCreatedAt(LocalDateTime.now());
        campus.setEligibleBranch(campus.getEligibleBranch());
        campusRepository.save(campus);

        return campus;

    }
}
