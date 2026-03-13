package com.example.POD.Controller.TeacherController;

import com.example.POD.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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
