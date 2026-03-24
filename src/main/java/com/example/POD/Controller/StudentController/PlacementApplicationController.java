package com.example.POD.Controller.StudentController;


import com.example.POD.Service.PlacementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class PlacementApplicationController {

    private final PlacementApplicationService placementApplicationService;
    @PostMapping("/studentApplicationData/{userId}/{campusId}")

    public String PlacementDataSubmition(@PathVariable Long userId,@PathVariable Long campusId)
    {
      return  placementApplicationService.PlacementdataService(userId,campusId);
    }
}
