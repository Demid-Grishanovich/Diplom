package com.datacrowd.backend.dataset.model;

import com.datacrowd.backend.project.model.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "datasets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "processed_base_path", length = 500)
    private String processedBasePath;

    @Column(length = 50)
    private String type;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false, length = 30)
    private String status; // NEW / PROCESSING / READY / FAILED / DELETED ...

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;
}
