package com.example.POD.Controller;

import com.example.POD.DTO.AnswerDTO;
import com.example.POD.Service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/User")
public class QuestionAndAnsController {
    private final AnswerService answerService;
    @PostMapping("/request-reply")
    public List<AnswerDTO> requestReply(@RequestBody String question)
    {
        return answerService.requestResponse(question);
    }
}
