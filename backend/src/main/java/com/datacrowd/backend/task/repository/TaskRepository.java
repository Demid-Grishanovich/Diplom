package com.datacrowd.backend.task.repository;

import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProject(Project project);

    List<Task> findAllByAssignedTo(User worker);

    @Query("""
           select t from Task t
           where t.project = :project
             and t.status = 'NEW'
             and t.assignedTo is null
           order by t.createdAt asc
           """)
    List<Task> findAvailableTasksForProject(Project project);
}
