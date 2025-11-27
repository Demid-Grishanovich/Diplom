package com.datacrowd.backend.project.service;

import com.datacrowd.backend.project.dto.ProjectCreateRequest;
import com.datacrowd.backend.project.dto.ProjectResponse;
import com.datacrowd.backend.project.dto.ProjectUpdateRequest;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.project.repository.ProjectRepository;
import com.datacrowd.backend.user.model.Role;
import com.datacrowd.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectCreateRequest request, User owner) {
        Instant now = Instant.now();

        Project project = Project.builder()
                .name(request.name())
                .description(request.description())
                .dataType(request.dataType())
                .owner(owner)
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();

        projectRepository.save(project);
        return toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getMyProjects(User owner) {
        return projectRepository.findAllByOwner(owner)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id, User currentUser) {
        Project project = getAccessibleProject(id, currentUser);
        return toResponse(project);
    }

    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request, User currentUser) {
        Project project = getAccessibleProject(id, currentUser);

        if (request.name() != null) {
            project.setName(request.name());
        }
        if (request.description() != null) {
            project.setDescription(request.description());
        }
        if (request.dataType() != null) {
            project.setDataType(request.dataType());
        }
        if (request.status() != null) {
            project.setStatus(request.status());
        }

        project.setUpdatedAt(Instant.now());

        projectRepository.save(project);
        return toResponse(project);
    }

    public ProjectResponse archiveProject(Long id, User currentUser) {
        Project project = getAccessibleProject(id, currentUser);
        project.setStatus("ARCHIVED");
        project.setUpdatedAt(Instant.now());
        projectRepository.save(project);
        return toResponse(project);
    }

    // ====== Вспомогательные методы ======

    private Project getAccessibleProject(Long id, User currentUser) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You don't have access to this project");
        }

        return project;
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDataType(),
                project.getStatus(),
                project.getOwner().getId(),
                project.getOwner().getEmail(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
