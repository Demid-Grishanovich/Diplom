package com.datacrowd.backend.worker.repository;

import com.datacrowd.backend.user.model.User;
import com.datacrowd.backend.worker.model.WorkerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerStatsRepository extends JpaRepository<WorkerStats, Long> {

    Optional<WorkerStats> findByWorker(User worker);
}
