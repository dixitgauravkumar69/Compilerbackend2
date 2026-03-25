package com.example.POD.Controller.StudentController;

import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Entity.Profile;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class GetLiveStreamTest {
    private final ProblemStatementRepo problemStatementRepo;
    private final ProfileRepository profileRepository;
    @GetMapping("/getLiveStream/{userId}")
    public List<ProblemStatement> getLiveProblems(@PathVariable Long userId)
    {

        Profile profile=profileRepository.findByUserUserid(userId);

        return problemStatementRepo.findByIsLiveTrueAndSemester(profile.getSemester());
    }
}
