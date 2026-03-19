package com.example.POD.Controller.TeacherController;

import com.example.POD.DTO.LiveDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Service.LiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faculty")
public class LiveController {
    private final LiveService liveService;
    @PostMapping("/live/{problemId}")

    public ProblemStatement doLive(@RequestBody LiveDTO liveDTO, @PathVariable Long problemId)
    {
       return liveService.problemLive(liveDTO,problemId);
    }

}
