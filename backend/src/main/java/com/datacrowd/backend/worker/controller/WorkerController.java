package com.datacrowd.backend.worker.controller;

import com.datacrowd.backend.user.model.User;
import com.datacrowd.backend.worker.dto.WorkerStatsResponse;
import com.datacrowd.backend.worker.service.WorkerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workers/me")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerStatsService workerStatsService;

    @GetMapping("/stats")
    public WorkerStatsResponse getMyStats(@AuthenticationPrincipal User currentUser) {
        return workerStatsService.getStatsForWorker(currentUser);
    }
}
