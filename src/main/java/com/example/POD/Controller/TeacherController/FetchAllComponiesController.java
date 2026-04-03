package com.example.POD.Controller.TeacherController;

import com.example.POD.Entity.CampusEntity;
import com.example.POD.Service.FetchComponiesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Getter
@Setter
@RequiredArgsConstructor
@RequestMapping("/api/teacher")
public class FetchAllComponiesController {
    private final FetchComponiesService fetchComponiesService;

    @GetMapping("/getAllComponies/{teacherId}")
    public List<CampusEntity> getComponiesInformation(@PathVariable Long teacherId)
    {
        return fetchComponiesService.getAllCompony(teacherId);
    }
}
