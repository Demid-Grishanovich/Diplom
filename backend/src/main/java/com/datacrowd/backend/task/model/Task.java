package com.datacrowd.backend.task.model;

import com.datacrowd.backend.dataset.model.Dataset;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dataset_id")
    private Dataset dataset;

    @Column(length = 50)
    private String type;

    @Column(name = "payload_ref", length = 500)
    private String payloadRef;

    @Column(name = "input_preview", columnDefinition = "text")
    private String inputPreview;

    @Column(nullable = false, length = 30)
    private String status; // NEW / LOCKED / COMPLETED / REJECTED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "reward_points", nullable = false)
    private Integer rewardPoints;
}
