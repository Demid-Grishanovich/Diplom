package com.datacrowd.backend.dataset.controller;

import com.datacrowd.backend.dataset.dto.DatasetCreateRequest;
import com.datacrowd.backend.dataset.dto.DatasetResponse;
import com.datacrowd.backend.dataset.dto.DatasetStatusResponse;
import com.datacrowd.backend.dataset.dto.GenerateTasksRequest;
import com.datacrowd.backend.dataset.service.DatasetService;
import com.datacrowd.backend.task.dto.TaskResponse;
import com.datacrowd.backend.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;

    // POST /projects/{projectId}/datasets
    @PostMapping("/projects/{projectId}/datasets")
    public ResponseEntity<DatasetResponse> createDataset(
            @PathVariable Long projectId,
            @Valid @RequestBody DatasetCreateRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        DatasetResponse response = datasetService.createDataset(projectId, request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /projects/{projectId}/datasets
    @GetMapping("/projects/{projectId}/datasets")
    public List<DatasetResponse> getProjectDatasets(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return datasetService.getProjectDatasets(projectId, currentUser);
    }

    // GET /datasets/{datasetId}
    @GetMapping("/datasets/{datasetId}")
    public DatasetResponse getDatasetById(
            @PathVariable Long datasetId,
            @AuthenticationPrincipal User currentUser
    ) {
        return datasetService.getDatasetById(datasetId, currentUser);
    }

    // DELETE /datasets/{datasetId}
    @DeleteMapping("/datasets/{datasetId}")
    public ResponseEntity<Void> deleteDataset(
            @PathVariable Long datasetId,
            @AuthenticationPrincipal User currentUser
    ) {
        datasetService.deleteDataset(datasetId, currentUser);
        return ResponseEntity.noContent().build();
    }
    // GET /datasets/{datasetId}/status
    @GetMapping("/datasets/{datasetId}/status")
    public DatasetStatusResponse getDatasetStatus(
            @PathVariable Long datasetId,
            @AuthenticationPrincipal User currentUser
    ) {
        return datasetService.getStatus(datasetId, currentUser);
    }

    // POST /datasets/{datasetId}/generate-tasks
    @PostMapping("/datasets/{datasetId}/generate-tasks")
    public List<TaskResponse> generateTasks(
            @PathVariable Long datasetId,
            @Valid @RequestBody GenerateTasksRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return datasetService.generateTasks(datasetId, request, currentUser);
    }
}
