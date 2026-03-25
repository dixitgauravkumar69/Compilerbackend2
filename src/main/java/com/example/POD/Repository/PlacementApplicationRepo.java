package com.example.POD.Repository;

import com.example.POD.DTO.AppliedStudentsDTO;
import com.example.POD.Entity.PlacementApplicationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlacementApplicationRepo extends JpaRepository<PlacementApplicationData,Long> {


    PlacementApplicationData findByUserUserid(Long userId);


    @Query("SELECT p FROM PlacementApplicationData p WHERE p.campus.id = :campusId")
    List<PlacementApplicationData> findByCampusId(@Param("campusId") Long campusId);

}
