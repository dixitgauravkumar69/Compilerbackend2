package com.example.POD.Controller.StudentController;


import com.example.POD.Service.PlacementApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/studentApplication/isApplied/{userId}")
    public Boolean isApplied(@PathVariable Long userId)
    {
        return placementApplicationService.isApplied(userId);
    }

}
