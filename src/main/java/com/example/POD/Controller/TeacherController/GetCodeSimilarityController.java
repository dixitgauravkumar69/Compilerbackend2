package com.example.POD.Controller.TeacherController;

import com.example.POD.DTO.GettingCodeSimilarityResponse;
import com.example.POD.Service.SaveCodeInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class GetCodeSimilarityController {
    private final SaveCodeInfoService saveCodeInfoService;

    @Cacheable(value = "SimilarCodeInfo", key = "#userId")
    @GetMapping("/getCodeSimilarity/{userId}/{problemId}")

    public List<GettingCodeSimilarityResponse> getCodeSimilarity(@PathVariable Long userId, @PathVariable Long problemId)
    {
        return saveCodeInfoService.getSimilarityCodeWithUser(userId,problemId);
    }

}
