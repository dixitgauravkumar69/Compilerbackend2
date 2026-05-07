package com.example.POD.Service;

import com.example.POD.DTO.AnswerDTO;
import com.example.POD.Entity.ProjectInfoEntity;
import com.example.POD.Repository.ProjectInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ProjectInfoRepo projectInfoRepo;
    private final GetEmbeding getEmbeding;

    public AnswerDTO requestResponse(String question) {

        // =========================
        // Question Preprocessing
        // =========================
        question = question.toLowerCase().trim();

        // =========================
        // Generate Question Embedding
        // =========================
        float[] embeddedQuestion = getEmbeding.callPythonAPI(question);

        // =========================
        // Load All Stored Embeddings
        // =========================
        List<ProjectInfoEntity> allEmbeddings = projectInfoRepo.findAll();

        System.out.println("========== ALL EMBEDDINGS ==========");

        for (ProjectInfoEntity entity : allEmbeddings) {
            System.out.println("Title : " + entity.getTitle());
        }

        // =========================
        // Best Match Variables
        // =========================
        double bestScore = -1;
        ProjectInfoEntity bestMatch = null;

        // =========================
        // Find Highest Similarity
        // =========================
        for (ProjectInfoEntity entity : allEmbeddings) {

            float[] storedEmbedding = entity.getEmbedding();

            // Skip invalid embeddings
            if (storedEmbedding == null ||
                    storedEmbedding.length != embeddedQuestion.length) {

                System.out.println("Invalid embedding found for: "
                        + entity.getTitle());

                continue;
            }

            double similarityScore =
                    similarityCount(storedEmbedding, embeddedQuestion);

            System.out.println("--------------------------------");
            System.out.println("Title : " + entity.getTitle());
            System.out.println("Similarity Score : " + similarityScore);

            // Store best match
            if (similarityScore > bestScore) {
                bestScore = similarityScore;
                bestMatch = entity;
            }
        }

        // =========================
        // Final Debugging
        // =========================
        System.out.println("================================");
        System.out.println("BEST SCORE : " + bestScore);

        if (bestMatch != null) {
            System.out.println("BEST MATCH TITLE : "
                    + bestMatch.getTitle());
        }

        // =========================
        // Prepare Response
        // =========================
        AnswerDTO answer = new AnswerDTO();
        answer.setQuestion(question);

        // Threshold
        double THRESHOLD = 0.80;

        if (bestScore >= THRESHOLD && bestMatch != null) {

            answer.setAnswer(bestMatch.getContent());

        } else {

            answer.setAnswer("Please contact with admin");
        }

        return answer;
    }

    // =====================================
    // COSINE SIMILARITY FUNCTION
    // =====================================
    public double similarityCount(float[] vecA, float[] vecB) {

        if (vecA == null || vecB == null ||
                vecA.length != vecB.length) {

            throw new IllegalArgumentException(
                    "Vectors must be non-null and same length"
            );
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {

            dotProduct += vecA[i] * vecB[i];

            normA += Math.pow(vecA[i], 2);

            normB += Math.pow(vecB[i], 2);
        }

        // Prevent divide by zero
        if (normA == 0 || normB == 0) {
            return 0.0;
        }

        return dotProduct /
                (Math.sqrt(normA) * Math.sqrt(normB));
    }
}