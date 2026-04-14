package com.example.POD.Service;

import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Repository.PlacementApplicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class GetJobApplicationStatusService {
    private final PlacementApplicationRepo placementApplicationRepo;
    public String getStatusofJobApplication(Long campusId,Long userId)
    {
        PlacementApplicationData placementData =placementApplicationRepo.findByCampusAndUser(campusId,userId);
        return placementData.getSelectionStatus();
    }

}
