package com.example.POD.Service;

import com.example.POD.DTO.LiveDTO;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.ProblemStatementRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // for logs in place of sout
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j // 2. Isse 'log' object mil jayega
@Service
@RequiredArgsConstructor
public class LiveService {
    private final ProblemStatementRepo problemStatementRepo;

    public ProblemStatement problemLive(LiveDTO liveDTO, Long problemId) {
        LocalDateTime startTime = liveDTO.getStartTime();
        LocalDateTime endTime = liveDTO.getEndTime();

        ProblemStatement problemForLive = problemStatementRepo.findById(problemId)
                .orElseThrow(() -> {
                    log.error("Problem with ID {} not found in database", problemId); // Error log
                    return new RuntimeException("Problem not found with id: " + problemId);
                });

        problemForLive.setStartTime(startTime);
        problemForLive.setEndTime(endTime);
        problemForLive.setIsLive(false);

        log.info("Set timing for Problem ID {}: Start={}, End={}", problemId, startTime, endTime);

        return problemStatementRepo.save(problemForLive);
    }

    @Scheduled(fixedRate = 60000)
    public void manageProblemLifecycle() {
        LocalDateTime now = LocalDateTime.now();
        log.info(">>> Lifecycle Scheduler Running at: {}", now);

        //Get all problems in allProblems list.....
        List<ProblemStatement> allProblems = problemStatementRepo.findAll();

        for (ProblemStatement problem : allProblems) {
            boolean isCurrentlyLive = (problem.getIsLive() != null) && problem.getIsLive();

            if (problem.getStartTime() == null || problem.getEndTime() == null) {
                continue;
            }

            // --- CASE 1: Problem ko LIVE karna ---
            if (!isCurrentlyLive && now.isAfter(problem.getStartTime()) && now.isBefore(problem.getEndTime())) {
                problem.setIsLive(true);
                problemStatementRepo.save(problem);
                log.info("[ACTIVATED] Problem ID {} is now LIVE!", problem.getId());
            }

            // --- CASE 2: Problem ko EXPIRE karna ---
            else if (isCurrentlyLive && now.isAfter(problem.getEndTime())) {
                problem.setIsLive(false);
                problemStatementRepo.save(problem);
                log.warn("[EXPIRED] Problem ID {} is now Offline.", problem.getId()); // Warn level for expiry
            }

            // --- CASE 3: Status check ---
            else if (isCurrentlyLive) {
                log.debug("Problem ID {} is currently running...", problem.getId());
            }
        }
        log.info(">>> Cycle Completed.");
    }
}