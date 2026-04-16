package com.example.POD.Service;

import com.example.POD.Controller.NotificationController;
import com.example.POD.DTO.LiveDTO;
import com.example.POD.Entity.NotificationEntity;
import com.example.POD.Entity.ProblemStatement;
import com.example.POD.Repository.NotificationRepository;
import com.example.POD.Repository.ProblemStatementRepo;
import com.example.POD.Repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // for logs in place of sout
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j // . Isse 'log' object mil jayega
@Service
@RequiredArgsConstructor
public class LiveService {
    private final ProblemStatementRepo problemStatementRepo;
    private final ProfileRepository profileRepository;
    private final NotificationController notificationController;
    private final NotificationRepository notificationRepository;

    public ProblemStatement problemLive(LiveDTO liveDTO, Long problemId) {

        //  Convert IST → UTC
        ZonedDateTime startIST = liveDTO.getStartTime().atZone(ZoneId.of("Asia/Kolkata"));
        ZonedDateTime endIST = liveDTO.getEndTime().atZone(ZoneId.of("Asia/Kolkata"));

        LocalDateTime startTimeUTC = startIST.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endTimeUTC = endIST.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        ProblemStatement problemForLive = problemStatementRepo.findById(problemId)
                .orElseThrow(() -> {
                    log.error("Problem with ID {} not found in database", problemId);
                    return new RuntimeException("Problem not found with id: " + problemId);
                });

        problemForLive.setStartTime(startTimeUTC);
        problemForLive.setEndTime(endTimeUTC);
        problemForLive.setIsLive(false);


        // yhi pe kr skte hai all ke liye ki check krke sbse phle semester ek specific hai matlb koi ank set kr lunga vo hai to another type se add kr lo

        problemForLive.setSemester(liveDTO.getSemester());


        log.info("Set timing for Problem ID {} (UTC): Start={}, End={}", problemId, startTimeUTC, endTimeUTC);


        ProblemStatement problem=problemStatementRepo.save(problemForLive);


        //Logic for sending notification ........
        List<Long>users=profileRepository.findBySemester(problem.getSemester());

        for(Long user:users)
        {
            NotificationEntity notificationEntity=new NotificationEntity();

           notificationController.sendToUser(user,"Problem is live solve fast");

            notificationEntity.setUserId(user);
            notificationEntity.setMessage("Problem is live now :"+problem.getId());
            notificationEntity.setType("liveProblem");


            notificationRepository.save(notificationEntity);
        }
        return problem ;
    }

    @Scheduled(fixedRate = 60000)
    public void manageProblemLifecycle() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
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