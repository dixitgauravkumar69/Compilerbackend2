package com.example.POD.Service;


import com.example.POD.Controller.NotificationController;
import com.example.POD.Entity.NotificationEntity;
import com.example.POD.Entity.PlacementApplicationData;
import com.example.POD.Repository.NotificationRepository;
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
    private final NotificationController notify;
    private final NotificationRepository notificationRepository;

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

      //SSE real time notification will send to this user
      notify.sendToUser(userId,"Application status is :"+currentStatus);

      //Save in db for while he reads...
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setUserId(userId);
        notificationEntity.setMessage("Application status is of :"+campusId+"is :" +currentStatus);
        notificationEntity.setType("updatedApplicationStatus"+campusId);
        notificationRepository.save(notificationEntity);


        return ResponseEntity.ok(
                "Current status is: " + studentCampusData.getSelectionStatus() + " updated successfully"
        );

    }

}
