package com.datacrowd.backend.dataset.service;

import com.datacrowd.backend.dataset.dto.DatasetCreateRequest;
import com.datacrowd.backend.dataset.dto.DatasetResponse;
import com.datacrowd.backend.dataset.dto.DatasetStatusResponse;
import com.datacrowd.backend.dataset.dto.GenerateTasksRequest;
import com.datacrowd.backend.dataset.model.Dataset;
import com.datacrowd.backend.dataset.repository.DatasetRepository;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.project.repository.ProjectRepository;
import com.datacrowd.backend.task.dto.TaskResponse;
import com.datacrowd.backend.task.service.TaskService;
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
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final ProjectRepository projectRepository;
    private final TaskService taskService;

    public DatasetResponse createDataset(Long projectId, DatasetCreateRequest request, User currentUser) {
        Project project = getAccessibleProject(projectId, currentUser);

        Instant now = Instant.now();

        Dataset dataset = Dataset.builder()
                .project(project)
                .filePath(request.filePath())
                .processedBasePath(request.processedBasePath())
                .type(request.type())
                .version(1)
                .status("NEW")
                .createdAt(now)
                .build();

        datasetRepository.save(dataset);
        return toResponse(dataset);
    }

    @Transactional(readOnly = true)
    public List<DatasetResponse> getProjectDatasets(Long projectId, User currentUser) {
        Project project = getAccessibleProject(projectId, currentUser);

        return datasetRepository.findAllByProject(project)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DatasetResponse getDatasetById(Long datasetId, User currentUser) {
        Dataset dataset = getAccessibleDataset(datasetId, currentUser);
        return toResponse(dataset);
    }

    public void deleteDataset(Long datasetId, User currentUser) {
        Dataset dataset = getAccessibleDataset(datasetId, currentUser);
        // можешь помечать DELETED, а не удалять физически:
        dataset.setStatus("DELETED");
        dataset.setProcessedAt(Instant.now());
        datasetRepository.save(dataset);
        // или datasetRepository.delete(dataset);
    }

    // ===== Вспомогательные методы =====

    private Project getAccessibleProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You don't have access to this project");
        }

        return project;
    }

    private Dataset getAccessibleDataset(Long datasetId, User currentUser) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        Project project = dataset.getProject();

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You don't have access to this dataset");
        }

        return dataset;
    }

    private DatasetResponse toResponse(Dataset dataset) {
        return new DatasetResponse(
                dataset.getId(),
                dataset.getProject().getId(),
                dataset.getFilePath(),
                dataset.getProcessedBasePath(),
                dataset.getType(),
                dataset.getVersion(),
                dataset.getStatus(),
                dataset.getCreatedAt(),
                dataset.getProcessedAt()
        );
    }
    public DatasetStatusResponse getStatus(Long datasetId, User currentUser) {
        Dataset dataset = getAccessibleDataset(datasetId, currentUser);
        return new DatasetStatusResponse(dataset.getId(), dataset.getStatus());
    }
    public List<TaskResponse> generateTasks(Long datasetId,
                                            GenerateTasksRequest request,
                                            User currentUser) {
        return taskService.generateTasksForDataset(
                datasetId,
                request.count(),
                request.type(),
                currentUser,
                request.rewardPoints()   // ← новый аргумент
        );
    }
}
