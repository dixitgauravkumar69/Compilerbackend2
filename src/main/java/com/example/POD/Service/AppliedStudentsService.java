package com.example.POD.Service;


import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Repository.PlacementApplicationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    @CacheEvict(value = "appliedStudentsCache", key = "#campusId")
    public ResponseEntity selectionStatusChange(Long campusId,Long userId,String currentStatus)
    {
      PlacementApplicationData studentCampusData= placementApplicationRepo.findByCampusAndUser(campusId,userId);

      if(studentCampusData==null)
      {
          return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(" No application found");
      }
      studentCampusData.setSelectionStatus(currentStatus);
      placementApplicationRepo.save(studentCampusData);
        return ResponseEntity.ok(
                "Current status is: " + studentCampusData.getSelectionStatus() + " updated successfully"
        );

    }

}
