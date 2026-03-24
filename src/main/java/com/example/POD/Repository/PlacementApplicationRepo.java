package com.example.POD.Repository;

import com.example.POD.Entity.PlacementApplicationData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementApplicationRepo extends JpaRepository<PlacementApplicationData,Long> {


    PlacementApplicationData findByUserUserid(Long userId);
}
