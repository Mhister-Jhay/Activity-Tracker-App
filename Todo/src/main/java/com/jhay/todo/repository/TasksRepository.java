package com.jhay.todo.repository;


import com.jhay.todo.enums.TaskEnum;
import com.jhay.todo.model.Tasks;
import com.jhay.todo.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks,Long> {
    Optional<Tasks> findTasksByUserAndTitle(Users users, String title);
    Page<Tasks> findAllByUserAndStatus(Users users, TaskEnum status, Pageable pageable);
    Page<Tasks> findAllByUserAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            Users user, String title, String description, Pageable pageable);
    Page<Tasks> findAllByUserId(Long user_id, Pageable pageable);


}
