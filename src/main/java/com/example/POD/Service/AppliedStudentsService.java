package com.example.POD.Service;

import com.example.POD.DTO.AppliedStudentsDTO;
import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Repository.PlacementApplicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppliedStudentsService {
    private final PlacementApplicationRepo placementApplicationRepo;
    public List<PlacementApplicationData> appliedStudent(Long campusId)
    {
        List<PlacementApplicationData> appliedStudents=placementApplicationRepo.findByCampusId(campusId);
        return appliedStudents;
    }

}
