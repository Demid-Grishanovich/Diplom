package com.datacrowd.backend.answer.repository;

import com.datacrowd.backend.answer.model.Answer;
import com.datacrowd.backend.project.model.Project;
import com.datacrowd.backend.task.model.Task;
import com.datacrowd.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByTask(Task task);

    @Query("""
           select a from Answer a
           where a.task.project = :project
           """)
    List<Answer> findAllByProject(Project project);

    List<Answer> findAllByWorker(User worker);
}
