package com.example.POD.Service;

import com.example.POD.DTO.AnswerDTO;
import com.example.POD.Entity.ProjectInfoEntity;
import com.example.POD.Repository.ProjectInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AnswerService {
    private final ProjectInfoRepo projectInfoRepo;
    private final GetEmbeding getEmbeding;

    public List<AnswerDTO> requestResponse(String question)
    {
        List<AnswerDTO>answers=new ArrayList<>();
        //Generating embedding of question

        float[]embedded_question= getEmbeding.callPythonAPI(question);

        //Load all embeddings from projectInfo

        List<ProjectInfoEntity> allEmbeddings=projectInfoRepo.findAll();


        for(ProjectInfoEntity Embedding:allEmbeddings)
        {
            AnswerDTO answer=new AnswerDTO();
            double similarity_score=similarityCount(Embedding.getEmbedding(),embedded_question);
            if(similarity_score>0.65)
            {
                answer.setQuestion(question);
                answer.setAnswer(Embedding.getContent());
                answers.add(answer);
            }


        }


        return answers;
    }
    public  double similarityCount(float[] vecA, float[] vecB) {

        if (vecA == null || vecB == null || vecA.length != vecB.length) {
            throw new IllegalArgumentException("Vectors must be non-null and same length");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            dotProduct += vecA[i] * vecB[i];
            normA += vecA[i] * vecA[i];
            normB += vecB[i] * vecB[i];
        }

        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
