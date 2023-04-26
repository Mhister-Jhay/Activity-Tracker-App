package com.jhay.todo.service;

import com.jhay.todo.dto.TasksDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TasksService {
    boolean createTask(TasksDTO tasksDTO, Long user_id);

    Page<TasksDTO> viewAllTask(Long user_id, int pageNum, int pageSize);

    Page<TasksDTO> viewAllTaskByStatus(Long user_id, String status, int pageNum, int pageSize);

    Long getTotalTaskByStatus(Long user_id, String status, int pageNum, int pageSize);

    TasksDTO viewSpecificTask(Long task__id);

    void deleteTask(Long task_id);

    Page<TasksDTO> searchForTask(Long user_id, String keyword, int pageNum, int pageSize);
}
