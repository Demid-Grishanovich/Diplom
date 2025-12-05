package com.datacrowd.backend.answer.controller;

import com.datacrowd.backend.answer.dto.AnswerResponse;
import com.datacrowd.backend.answer.service.AnswerService;
import com.datacrowd.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/answers/{answerId}")
    public AnswerResponse getAnswerById(
            @PathVariable Long answerId,
            @AuthenticationPrincipal User currentUser
    ) {
        return answerService.getAnswerById(answerId, currentUser);
    }

    @GetMapping("/tasks/{taskId}/answers")
    public List<AnswerResponse> getAnswersByTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        return answerService.getAnswersByTask(taskId, currentUser);
    }

    @GetMapping("/projects/{projectId}/answers")
    public List<AnswerResponse> getAnswersByProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return answerService.getAnswersByProject(projectId, currentUser);
    }

    @PostMapping("/answers/{answerId}/accept")
    public AnswerResponse acceptAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal User currentUser
    ) {
        return answerService.acceptAnswer(answerId, currentUser);
    }
}
