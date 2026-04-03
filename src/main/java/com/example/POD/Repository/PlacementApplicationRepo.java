package com.example.POD.Repository;


import com.example.POD.Entity.PlacementApplicationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlacementApplicationRepo extends JpaRepository<PlacementApplicationData,Long> {


    @org.springframework.cache.annotation.Cacheable(value = "userPlacementCache", key = "#userId")
    PlacementApplicationData findByUserUserid(Long userId);


    @org.springframework.cache.annotation.Cacheable(value = "appliedStudentsCache", key = "#campusId")
    @Query("SELECT p FROM PlacementApplicationData p WHERE p.campus.id = :campusId")
    List<PlacementApplicationData> findByCampusId(@Param("campusId") Long campusId);

}
