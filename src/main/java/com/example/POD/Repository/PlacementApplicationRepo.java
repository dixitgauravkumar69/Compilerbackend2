package com.example.POD.Repository;


import com.example.POD.Entity.PlacementApplicationData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlacementApplicationRepo extends JpaRepository<PlacementApplicationData,Long> {


    @Cacheable(value = "userPlacementCache", key = "#userId")
    PlacementApplicationData findByUserUserid(Long userId);


    @Cacheable(value = "appliedStudentsCache", key = "#campusId")
    @Query("SELECT p FROM PlacementApplicationData p WHERE p.campus.id = :campusId")
    List<PlacementApplicationData> findByCampusId(@Param("campusId") Long campusId);

    @Query("SELECT p FROM PlacementApplicationData p WHERE p.campus.id = :campusId AND p.user.id = :userId")
    PlacementApplicationData findByCampusAndUser(@Param("campusId") Long campusId,
                                                 @Param("userId") Long userId);


}
