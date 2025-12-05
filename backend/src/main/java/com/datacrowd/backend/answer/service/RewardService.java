package com.datacrowd.backend.answer.service;

import com.datacrowd.backend.answer.model.Answer;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.worker.model.WorkerStats;
import com.datacrowd.backend.worker.repository.WorkerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final WorkerStatsRepository workerStatsRepository;

    @Transactional
    public void grantRewardIfNeeded(Answer answer) {
        // уже выдали раньше — ничего не делаем
        if (Boolean.TRUE.equals(answer.getRewardGranted())) {
            return;
        }

        // если ответ не принят, тоже ничего не даём
        if (!Boolean.TRUE.equals(answer.getAccepted())) {
            return;
        }

        Task task = answer.getTask();
        Integer rewardPoints = task.getRewardPoints();
        int points = rewardPoints != null ? rewardPoints : 0;

        if (points <= 0) {
            answer.setRewardGranted(true);
            return;
        }

        // достаём/создаём статистику воркера
        WorkerStats stats = workerStatsRepository
                .findByWorkerId(answer.getWorker().getId())
                .orElseGet(() -> workerStatsRepository.save(
                        WorkerStats.builder()
                                .worker(answer.getWorker())
                                .tasksCompleted(0)
                                .avgScore(null)
                                .lastActive(answer.getCreatedAt())
                                .pointsBalance(0L)
                                .totalPointsEarned(0L)
                                .build()
                ));

        long newBalance = stats.getPointsBalance() + points;
        long newTotal = stats.getTotalPointsEarned() + points;

        stats.setPointsBalance(newBalance);
        stats.setTotalPointsEarned(newTotal);
        workerStatsRepository.save(stats);

        // помечаем, что награда выдана
        answer.setRewardGranted(true);
    }
}
