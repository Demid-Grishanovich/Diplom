package com.datacrowd.backend.answer.service;

import com.datacrowd.backend.answer.dto.AnswerResponse;
import com.datacrowd.backend.answer.model.Answer;
import com.datacrowd.backend.answer.repository.AnswerRepository;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.project.repository.ProjectRepository;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.task.repository.TaskRepository;
import com.datacrowd.backend.user.model.Role;
import com.datacrowd.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final RewardService rewardService;

    public AnswerResponse getAnswerById(Long answerId, User currentUser) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        checkAccessToTask(answer.getTask(), currentUser);
        return toResponse(answer);
    }

    public List<AnswerResponse> getAnswersByTask(Long taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        checkAccessToTask(task, currentUser);

        return answerRepository.findAllByTask(task)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<AnswerResponse> getAnswersByProject(Long projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("You don't have access to this project");
        }

        return answerRepository.findAllByProject(project)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void checkAccessToTask(Task task, User currentUser) {
        switch (currentUser.getRole()) {
            case ADMIN -> {}
            case CLIENT -> {
                if (!task.getProject().getOwner().getId().equals(currentUser.getId())) {
                    throw new SecurityException("You don't have access to this task answers");
                }
            }
            case WORKER -> {
                if (task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(currentUser.getId())) {
                    throw new SecurityException("You don't have access to this task answers");
                }
            }
        }
    }

    private AnswerResponse toResponse(Answer answer) {
        return new AnswerResponse(
                answer.getId(),
                answer.getTask().getId(),
                answer.getWorker().getId(),
                answer.getAnswerJson(),
                answer.getEvaluationScore(),
                answer.getAccepted(),
                answer.getCreatedAt()
        );
    }
    @Transactional
    public AnswerResponse acceptAnswer(Long answerId, User currentUser) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        answer.setAccepted(true);
        rewardService.grantRewardIfNeeded(answer);
        answerRepository.save(answer);
        return toResponse(answer);
    }
}
