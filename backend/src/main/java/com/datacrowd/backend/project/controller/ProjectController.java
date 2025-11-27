package com.datacrowd.backend.project.controller;

import com.datacrowd.backend.project.dto.ProjectCreateRequest;
import com.datacrowd.backend.project.dto.ProjectResponse;
import com.datacrowd.backend.project.dto.ProjectUpdateRequest;
import com.datacrowd.backend.project.service.ProjectService;
import com.datacrowd.backend.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Создать проект (для залогиненного клиента)
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        ProjectResponse response = projectService.createProject(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Список моих проектов
    @GetMapping
    public List<ProjectResponse> getMyProjects(@AuthenticationPrincipal User currentUser) {
        return projectService.getMyProjects(currentUser);
    }

    // Получить проект по id (если владелец или админ)
    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return projectService.getProjectById(projectId, currentUser);
    }

    // Частичное обновление проекта
    @PatchMapping("/{projectId}")
    public ProjectResponse updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return projectService.updateProject(projectId, request, currentUser);
    }

    // Архивировать проект (можно сделать отдельный эндпоинт)
    @PostMapping("/{projectId}/archive")
    public ProjectResponse archiveProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return projectService.archiveProject(projectId, currentUser);
    }
}
