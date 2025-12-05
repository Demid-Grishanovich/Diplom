package com.datacrowd.backend.worker.model;

import com.datacrowd.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "worker_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // worker_id : bigint «FK»
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    // tasks_completed : int
    @Column(name = "tasks_completed", nullable = false)
    private Integer tasksCompleted;

    // avg_score : float
    @Column(name = "avg_score")
    private Double avgScore;

    // last_active : timestamp
    @Column(name = "last_active")
    private Instant lastActive;

    @Column(name = "points_balance", nullable = false)
    private Long pointsBalance;

    @Column(name = "total_points_earned", nullable = false)
    private Long totalPointsEarned;
}
