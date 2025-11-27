package com.datacrowd.backend.analytics.service;

import com.datacrowd.backend.analytics.dto.GlobalAnalyticsResponse;
import com.datacrowd.backend.analytics.dto.ProjectAnalyticsResponse;
import com.datacrowd.backend.dataset.repository.DatasetRepository;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.project.repository.ProjectRepository;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.task.repository.TaskRepository;
import com.datacrowd.backend.user.model.Role;
import com.datacrowd.backend.user.model.User;
import com.datacrowd.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final DatasetRepository datasetRepository;
    private final TaskRepository taskRepository;

    public GlobalAnalyticsResponse getGlobalAnalytics() {
        long totalUsers = userRepository.count();
        long totalProjects = projectRepository.count();
        long totalDatasets = datasetRepository.count();
        long totalTasks = taskRepository.count();
        // ответы считаем по задачам с COMPLETED (если нужно, можно отдельный AnswerRepository.count())
        long totalAnswers = taskRepository.findAll().stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .count();

        return new GlobalAnalyticsResponse(
                totalUsers,
                totalProjects,
                totalDatasets,
                totalTasks,
                totalAnswers
        );
    }

    public ProjectAnalyticsResponse getProjectAnalytics(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You don't have access to this project analytics");
        }

        var tasks = taskRepository.findAllByProject(project);

        long totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
        long rejectedTasks = tasks.stream().filter(t -> "REJECTED".equals(t.getStatus())).count();

        long activeDatasets = datasetRepository.findAllByProject(project).stream()
                .filter(d -> !"DELETED".equalsIgnoreCase(d.getStatus()))
                .count();

        return new ProjectAnalyticsResponse(
                project.getId(),
                project.getName(),
                totalTasks,
                completedTasks,
                rejectedTasks,
                activeDatasets
        );
    }
}
