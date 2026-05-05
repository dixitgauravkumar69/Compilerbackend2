package com.example.POD.Service;

import com.example.POD.Entity.ProjectInfoEntity;
import com.example.POD.Repository.ProjectInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AddProjectInfoService {
    private final ProjectInfoRepo projectInfoRepo;
    private final GetEmbeding getEmbeding;
    public String addProjectInfo(String title,String content)
    {
        ProjectInfoEntity projectInfoData=new ProjectInfoEntity();
        if(title==null||content==null)
        {
            return "Please fill all fields";
        }
        projectInfoData.setTitle(title);
        projectInfoData.setContent(content);

        String fullData=content.concat(title);

        //Genereate embadding of title+content this will save in db

        float[] embedData=generateEmbedding(fullData);

        projectInfoData.setEmbedding(embedData);
        projectInfoRepo.save(projectInfoData);


        return "Project Info added Successfully";

    }

    public float[] generateEmbedding(String textContent)
    {
       return getEmbeding.callPythonAPI(textContent);
    }

}
