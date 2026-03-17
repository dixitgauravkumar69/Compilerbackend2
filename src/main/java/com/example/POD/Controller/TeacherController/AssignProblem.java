package com.example.POD.Controller.TeacherController;

import com.example.POD.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/teacher")
public class AssignProblem {
    private final UserService userService;
    @GetMapping("/assignProblem/{problemId}")
    public String assignProblem(@PathVariable Long problemId)
    {
        String mess=userService.assignProblemStatement(problemId);
        return mess;
    }
}
