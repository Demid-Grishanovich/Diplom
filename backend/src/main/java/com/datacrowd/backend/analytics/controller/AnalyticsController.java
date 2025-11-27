package com.datacrowd.backend.analytics.controller;

import com.datacrowd.backend.analytics.dto.GlobalAnalyticsResponse;
import com.datacrowd.backend.analytics.dto.ProjectAnalyticsResponse;
import com.datacrowd.backend.analytics.service.AnalyticsService;
import com.datacrowd.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // GET /analytics/global
    @GetMapping("/global")
    public GlobalAnalyticsResponse getGlobal() {
        return analyticsService.getGlobalAnalytics();
    }

    // GET /projects/{projectId}/analytics
    @GetMapping("/projects/{projectId}")
    public ProjectAnalyticsResponse getProjectAnalytics(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return analyticsService.getProjectAnalytics(projectId, currentUser);
    }
}
