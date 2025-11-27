package com.datacrowd.backend.project.repository;

import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByOwner(User owner);
}
