package com.example.POD.Controller.StudentController;


import com.example.POD.Service.GetJobApplicationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class GetJobApplicationStatus {
    private final GetJobApplicationStatusService getJobApplicationStatusService;
    @GetMapping("/jobSelection/{campusId}/{userId}")
    public String jobSelectionStatus(@PathVariable Long campusId,@PathVariable Long userId)
    {
       return getJobApplicationStatusService.getStatusofJobApplication(campusId,userId);
    }

}
