package com.example.POD.Controller.Admin;

import com.example.POD.DTO.AddProjectInfoDTO;
import com.example.POD.DTO.ProjectInfoResDTO;
import com.example.POD.Service.AddProjectInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AddProjectInfo {

    private final AddProjectInfoService addProjectInfoService;
    @PostMapping("/add/info")
    public ProjectInfoResDTO addProjectInfo(@RequestBody AddProjectInfoDTO projectInfo)
    {
        ProjectInfoResDTO projectInfoResDTO=new ProjectInfoResDTO();
       String res=addProjectInfoService.addProjectInfo(projectInfo.getTitle(),projectInfo.getContent());
       projectInfoResDTO.setProjectInfoRes(res);

       return  projectInfoResDTO;

    }
}
