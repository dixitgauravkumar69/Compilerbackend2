package com.example.POD.Controller.StudentController;


import com.example.POD.Entity.ResumeEntity;
import com.example.POD.Repository.ResumeRepository;
import com.example.POD.Service.PdfService;
import com.example.POD.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class GetResumeByStudent {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;
    private final PdfService pdfService;
    @GetMapping("/getResume/{userId}")
    public List<ResumeEntity> getResumeInfo(@PathVariable Long userId)
    {
        return resumeRepository.findByUserUserid(userId);
    }

    @GetMapping("/downloadResume/{id}")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) throws Exception {

        ResumeEntity resume = resumeRepository.findById(id).orElseThrow();

        String html = resumeService.generateHtml(resume);

        byte[] pdf = pdfService.generatePdf(html);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
