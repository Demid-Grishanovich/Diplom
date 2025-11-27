package com.datacrowd.backend.dataset.repository;

import com.datacrowd.backend.dataset.model.Dataset;
import com.datacrowd.backend.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long> {

    List<Dataset> findAllByProject(Project project);
}
