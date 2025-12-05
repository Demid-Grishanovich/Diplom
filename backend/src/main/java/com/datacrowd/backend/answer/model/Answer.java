package com.datacrowd.backend.answer.model;

import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private User worker;

    @Column(name = "answer_json", columnDefinition = "jsonb", nullable = false)
    private String answerJson;

    @Column(name = "evaluation_score")
    private Double evaluationScore;

    @Column(name = "is_accepted")
    private Boolean accepted;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "reward_granted", nullable = false)
    private Boolean rewardGranted;

}
