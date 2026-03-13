package com.example.POD.Service;

import com.example.POD.DTO.ResumeDTO;
import com.example.POD.Entity.Profile;
import com.example.POD.Entity.ResumeEntity;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.ProfileRepository;
import com.example.POD.Repository.ResumeRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ProfileRepository profile;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    public ResumeEntity addResumeInfo(ResumeDTO resumeDTO, Long userId) {

        UserEntity userBasicInfo = userRepository.findByuserid(userId);
        Profile userProfileExtra = profile.findByUserUserid(userId);

        ResumeEntity resume = new ResumeEntity();

        resume.setUser(userBasicInfo);
        resume.setName(userBasicInfo.getUsername());
        resume.setEmail(userBasicInfo.getUserEmail());

        resume.setPhone(userProfileExtra.getPhone());
        resume.setCollege(userProfileExtra.getCollege());
        resume.setBranch(userProfileExtra.getBranch());
        resume.setCgpa(userProfileExtra.getCgpa());
        resume.setSkills(userProfileExtra.getSkills());
        resume.setGithub(userProfileExtra.getGithub());
        resume.setLinkedin(userProfileExtra.getLinkedin());

        resume.setProjects(resumeDTO.getProjects());
        resume.setInternships(resumeDTO.getInternships());
        resume.setBio(resumeDTO.getBio());
        resume.setSummary(resumeDTO.getSummary());
        resume.setAchievements(resumeDTO.getAchievements());

        return resumeRepository.save(resume);
    }

    public String generateHtml(ResumeEntity resume) throws Exception {

        // Template ko classpath se load karo (Docker compatible)
        ClassPathResource resource = new ClassPathResource("templates/resume-template.html");

        String html = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        html = html.replace("{{name}}", escapeXml(resume.getName()));
        html = html.replace("{{email}}", escapeXml(resume.getEmail()));
        html = html.replace("{{phone}}", escapeXml(resume.getPhone()));
        html = html.replace("{{bio}}", escapeXml(resume.getBio()));
        html = html.replace("{{summary}}", escapeXml(resume.getSummary()));

        html = html.replace("{{github}}", escapeXml(resume.getGithub()));
        html = html.replace("{{linkedin}}", escapeXml(resume.getLinkedin()));

        html = html.replace("{{college}}", escapeXml(resume.getCollege()));
        html = html.replace("{{branch}}", escapeXml(resume.getBranch()));
        html = html.replace("{{cgpa}}", String.valueOf(resume.getCgpa()));

        html = html.replace("{{skills}}", escapeXml(resume.getSkills()));
        html = html.replace("{{projects}}", escapeXml(resume.getProjects()));
        html = html.replace("{{internships}}", escapeXml(resume.getInternships()));
        html = html.replace("{{achievements}}", escapeXml(resume.getAchievements()));

        return html;
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}