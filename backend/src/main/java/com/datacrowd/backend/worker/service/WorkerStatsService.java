package com.datacrowd.backend.worker.service;

import com.datacrowd.backend.user.model.User;
import com.datacrowd.backend.worker.dto.WorkerStatsResponse;
import com.datacrowd.backend.worker.model.WorkerStats;
import com.datacrowd.backend.worker.repository.WorkerStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkerStatsService {

    private final WorkerStatsRepository workerStatsRepository;

    public void incrementTasksCompleted(User worker) {
        WorkerStats stats = workerStatsRepository.findByWorker(worker)
                .orElseGet(() -> WorkerStats.builder()
                        .worker(worker)
                        .tasksCompleted(0)
                        .avgScore(null)
                        .build()
                );

        stats.setTasksCompleted(stats.getTasksCompleted() + 1);
        stats.setLastActive(Instant.now());

        workerStatsRepository.save(stats);
    }

    @Transactional(readOnly = true)
    public WorkerStatsResponse getStatsForWorker(User worker) {
        WorkerStats stats = workerStatsRepository.findByWorker(worker)
                .orElseGet(() -> WorkerStats.builder()
                        .worker(worker)
                        .tasksCompleted(0)
                        .avgScore(null)
                        .lastActive(null)
                        .build()
                );

        return new WorkerStatsResponse(
                worker.getId(),
                worker.getEmail(),
                stats.getTasksCompleted(),
                stats.getAvgScore(),
                stats.getLastActive()
        );
    }
}
