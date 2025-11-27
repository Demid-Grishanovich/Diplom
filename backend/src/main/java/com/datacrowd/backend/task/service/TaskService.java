package com.datacrowd.backend.task.service;

import com.datacrowd.backend.answer.model.Answer;
import com.datacrowd.backend.answer.repository.AnswerRepository;
import com.datacrowd.backend.dataset.model.Dataset;
import com.datacrowd.backend.dataset.repository.DatasetRepository;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.project.repository.ProjectRepository;
import com.datacrowd.backend.task.dto.TaskResponse;
import com.datacrowd.backend.task.dto.TaskSubmitRequest;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.task.repository.TaskRepository;
import com.datacrowd.backend.user.model.Role;
import com.datacrowd.backend.user.model.User;
import com.datacrowd.backend.worker.service.WorkerStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final DatasetRepository datasetRepository;
    private final AnswerRepository answerRepository;
    private final WorkerStatsService workerStatsService;


    // --------- Для заказчика / админа ---------

    @Transactional(readOnly = true)
    public List<TaskResponse> getProjectTasks(Long projectId, User currentUser) {
        Project project = getAccessibleProject(projectId, currentUser);
        return taskRepository.findAllByProject(project)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId, User currentUser) {
        Task task = getAccessibleTask(taskId, currentUser);
        return toResponse(task);
    }

    // --------- Для воркера ---------

    @Transactional(readOnly = true)
    public TaskResponse getNextTask(Long projectId, User worker) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // worker тоже должен иметь доступ? здесь считаем, что любой WORKER
        List<Task> tasks = taskRepository.findAvailableTasksForProject(project);
        if (tasks.isEmpty()) {
            throw new IllegalStateException("No available tasks");
        }
        Task task = tasks.get(0);
        return toResponse(task);
    }

    public TaskResponse lockTask(Long taskId, User worker) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (task.getAssignedTo() != null && !task.getAssignedTo().getId().equals(worker.getId())) {
            throw new IllegalStateException("Task already locked by another worker");
        }
        if (!"NEW".equals(task.getStatus())) {
            throw new IllegalStateException("Task cannot be locked in status " + task.getStatus());
        }

        task.setAssignedTo(worker);
        task.setStatus("LOCKED");
        task.setUpdatedAt(Instant.now());
        taskRepository.save(task);

        return toResponse(task);
    }

    public TaskResponse unlockTask(Long taskId, User worker) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(worker.getId())) {
            throw new SecurityException("You can't unlock this task");
        }

        task.setAssignedTo(null);
        task.setStatus("NEW");
        task.setUpdatedAt(Instant.now());
        taskRepository.save(task);

        return toResponse(task);
    }

    public TaskResponse submitTask(Long taskId, TaskSubmitRequest request, User worker) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(worker.getId())) {
            throw new SecurityException("Task is not assigned to you");
        }

        // создаём Answer
        Answer answer = Answer.builder()
                .task(task)
                .worker(worker)
                .answerJson(request.answerJson())
                .createdAt(Instant.now())
                .build();
        answerRepository.save(answer);

        task.setStatus("COMPLETED");
        task.setUpdatedAt(Instant.now());
        taskRepository.save(task);
        workerStatsService.incrementTasksCompleted(worker);

        return toResponse(task);
    }

    public TaskResponse rejectTask(Long taskId, User currentUser) {
        Task task = getAccessibleTask(taskId, currentUser);
        task.setStatus("REJECTED");
        task.setUpdatedAt(Instant.now());
        taskRepository.save(task);
        return toResponse(task);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(User worker) {
        return taskRepository.findAllByAssignedTo(worker)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // --------- Generate tasks for dataset ---------

    public List<TaskResponse> generateTasksForDataset(Long datasetId, int count, String type, User currentUser) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));
        Project project = getAccessibleProject(dataset.getProject().getId(), currentUser);

        Instant now = Instant.now();
        for (int i = 0; i < count; i++) {
            Task task = Task.builder()
                    .project(project)
                    .dataset(dataset)
                    .type(type)
                    .payloadRef("item-" + (i + 1))
                    .inputPreview("Auto generated item " + (i + 1))
                    .status("NEW")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
            taskRepository.save(task);
        }

        return taskRepository.findAllByProject(project)
                .stream()
                .filter(t -> t.getDataset() != null && t.getDataset().getId().equals(datasetId))
                .map(this::toResponse)
                .toList();
    }

    // --------- helpers ---------

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

    private Task getAccessibleTask(Long taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        return switch (currentUser.getRole()) {
            case ADMIN -> task;
            case CLIENT -> {
                if (!task.getProject().getOwner().getId().equals(currentUser.getId())) {
                    throw new SecurityException("You don't have access to this task");
                }
                yield task;
            }
            case WORKER -> {
                if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(currentUser.getId())) {
                    throw new SecurityException("You don't have access to this task");
                }
                yield task;
            }
        };
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getProject().getId(),
                task.getDataset() != null ? task.getDataset().getId() : null,
                task.getType(),
                task.getPayloadRef(),
                task.getInputPreview(),
                task.getStatus(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
