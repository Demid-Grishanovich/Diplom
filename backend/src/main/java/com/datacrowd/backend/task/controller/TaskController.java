package com.datacrowd.backend.task.controller;

import com.datacrowd.backend.task.dto.TaskResponse;
import com.datacrowd.backend.task.dto.TaskSubmitRequest;
import com.datacrowd.backend.task.service.TaskService;
import com.datacrowd.backend.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Для заказчика: список задач проекта
    @GetMapping("/projects/{projectId}/tasks")
    public List<TaskResponse> getProjectTasks(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.getProjectTasks(projectId, currentUser);
    }

    // Детали задачи
    @GetMapping("/tasks/{taskId}")
    public TaskResponse getTaskById(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.getTaskById(taskId, currentUser);
    }

    // Для воркера: получить следующую доступную задачу проекта
    @GetMapping("/tasks/next")
    public TaskResponse getNextTask(
            @RequestParam Long projectId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.getNextTask(projectId, currentUser);
    }

    // Заблокировать (взять) задачу
    @PostMapping("/tasks/{taskId}/lock")
    public TaskResponse lockTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.lockTask(taskId, currentUser);
    }

    // Освободить задачу
    @PostMapping("/tasks/{taskId}/unlock")
    public TaskResponse unlockTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.unlockTask(taskId, currentUser);
    }

    // Отправить ответ по задаче
    @PostMapping("/tasks/{taskId}/submit")
    public TaskResponse submitTask(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskSubmitRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.submitTask(taskId, request, currentUser);
    }

    // Отклонить задачу (заказчик/админ)
    @PostMapping("/tasks/{taskId}/reject")
    public TaskResponse rejectTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        return taskService.rejectTask(taskId, currentUser);
    }

    // Задачи, назначенные на меня (воркер)
    @GetMapping("/workers/me/tasks")
    public List<TaskResponse> myTasks(@AuthenticationPrincipal User currentUser) {
        return taskService.getMyTasks(currentUser);
    }
}
