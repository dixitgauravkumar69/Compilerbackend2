package com.example.POD.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.POD.DTO.Jobdescription;
import com.example.POD.DTO.ShowJobsStudent;
import com.example.POD.Entity.CampusEntity;
import com.example.POD.Repository.CampusRepository;
import com.example.POD.Service.CampusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController

@RequiredArgsConstructor
@RequestMapping("/placement")
public class CampusController {

    private final CampusService campusService;
    private final CampusRepository campusRepository;
    @Autowired
    private Cloudinary  cloudinary; //  inject Cloudinary


    @PostMapping(value="/addJob", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CampusEntity addJob(
            @RequestPart("job") CampusEntity campus,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment
    ) {
        try {
            if (attachment != null && !attachment.isEmpty()) {

                // Step 1: MultipartFile ko temporary file me save karo
                File tempFile = File.createTempFile("upload-", attachment.getOriginalFilename());
                attachment.transferTo(tempFile);  // Transfer bytes to temp file

                // Step 2: Cloudinary upload
                Map uploadResult = cloudinary.uploader().upload(
                        tempFile,
                        ObjectUtils.asMap(
                                "resource_type", "raw",
                                "public_id", "jobs/" + attachment.getOriginalFilename(),
                                "use_filename", true,
                                "unique_filename", false
                        )
                );

                // Step 3: Get URL and save
                String fileUrl = uploadResult.get("secure_url").toString();
                campus.setAttachment(fileUrl);

                // Step 4: Delete temporary file
                tempFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return campusService.addJob(campus);
    }
    @GetMapping("/getJobInfo/{semester}")
    public List<ShowJobsStudent> getJobInfo(@PathVariable Integer semester)
    {

        List<CampusEntity> jobs = campusRepository.findBySemester(semester);

        return jobs.stream().map(job -> {

            ShowJobsStudent dto = new ShowJobsStudent();

            dto.setId(job.getId());
            dto.setCompany(job.getCompany());
            dto.setTitle(job.getTitle());
            dto.setJobType(job.getJobType());
            dto.setIndustry(job.getIndustry());
            dto.setLocation(job.getLocation());
            dto.setRegistrationLastDate(job.getRegistrationLastDate());
            dto.setAttachment(job.getAttachment());

            return dto;

        }).toList();

    }


    @GetMapping("/student/job/{id}")
    public Jobdescription getJobDescription(@PathVariable Long id){
        return campusRepository.getJobDescription(id);
    }

}