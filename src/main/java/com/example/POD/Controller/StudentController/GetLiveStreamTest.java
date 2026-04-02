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

import java.util.ArrayList;
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

        List<ProblemStatement>challenges;
        List<ProblemStatement>challenges2;

             challenges=  problemStatementRepo.findByIsLiveTrueAndSemester(0);

        challenges2= problemStatementRepo.findByIsLiveTrueAndSemester(profile.getSemester());

        List<ProblemStatement> finalList = new ArrayList<>();
        finalList.addAll(challenges);
        finalList.addAll(challenges2);

        return finalList;

    }
}
