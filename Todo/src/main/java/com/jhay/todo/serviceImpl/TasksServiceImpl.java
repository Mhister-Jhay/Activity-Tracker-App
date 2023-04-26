package com.jhay.todo.serviceImpl;

import com.jhay.todo.dto.TasksDTO;
import com.jhay.todo.enums.TaskEnum;
import com.jhay.todo.exception.NotFoundException;
import com.jhay.todo.model.Tasks;
import com.jhay.todo.model.Users;
import com.jhay.todo.repository.TasksRepository;
import com.jhay.todo.repository.UsersRepository;
import com.jhay.todo.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final UsersRepository usersRepository;

    @Override
    public boolean createTask(TasksDTO tasksDTO, Long user_id) {
        boolean isTaskCreated = false;
        Users users = usersRepository.findUsersById(user_id);
        Optional<Tasks> optionalTasks = tasksRepository.findTasksByUserAndTitle(users,tasksDTO.getTitle());
        if(optionalTasks.isEmpty()){
            tasksDTO.setCreatedAt(saveLocalDate(LocalDateTime.now()));
            Tasks tasks = Tasks.builder()
                    .title(tasksDTO.getTitle())
                    .description(tasksDTO.getDescription())
                    .status(TaskEnum.PENDING)
                    .createdAt(tasksDTO.getCreatedAt())
                    .user(users)
                    .build();
            tasksRepository.save(tasks);
            isTaskCreated = true;
        }

        return isTaskCreated;
    }

    @Override
    public Page<TasksDTO> viewAllTask(Long user_id, int pageNum, int pageSize) {
        Users users = usersRepository.findUsersById(user_id);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("updatedAt").descending());
        Page<Tasks> tasksPage = tasksRepository.findAllByUserId(user_id, pageable);
        List<TasksDTO> tasksDTOList = tasksPage.stream().map(c->TasksDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .completedAt(c.getCompletedAt())
                .user(users)
                .build()).toList();
        return new PageImpl<>(tasksDTOList,pageable,tasksPage.getTotalElements());
    }

    @Override
    public Page<TasksDTO> viewAllTaskByStatus(Long user_id, String status, int pageNum, int pageSize) {
        Users users = usersRepository.findUsersById(user_id);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("updatedAt").descending());
        Page<Tasks> tasksPage = tasksRepository.findAllByUserAndStatus(users, TaskEnum.valueOf(status.toUpperCase()),pageable);
        List<TasksDTO> tasksDTOList = tasksPage.stream().map(c->TasksDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .completedAt(c.getCompletedAt())
                .build()).toList();
        return new PageImpl<>(tasksDTOList,pageable,tasksPage.getTotalElements());
    }


    @Override
    public Long getTotalTaskByStatus(Long user_id, String status, int pageNum, int pageSize){
        return viewAllTaskByStatus(user_id,status,pageNum,pageSize).getTotalElements();
    }

    @Override
    public TasksDTO viewSpecificTask(Long task_id) {
        Optional<Tasks> optionalTasks = tasksRepository.findById(task_id);
        if(optionalTasks.isPresent()){
            Tasks tasks = optionalTasks.get();
            return TasksDTO.builder()
                    .id(tasks.getId())
                    .title(tasks.getTitle())
                    .description(tasks.getDescription())
                    .status(tasks.getStatus())
                    .createdAt(tasks.getCreatedAt())
                    .updatedAt(tasks.getUpdatedAt())
                    .completedAt(tasks.getCompletedAt())
                    .build();
        }
        return null;
    }
    @Override
    public void deleteTask(Long task_id){
        tasksRepository.deleteById(task_id);
    }

    @Override
    public Page<TasksDTO> searchForTask(Long user_id, String keyword, int pageNum, int pageSize){
        Users users = usersRepository.findUsersById(user_id);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("updatedAt").descending());
        Page<Tasks> tasksPage =
            tasksRepository.findAllByUserAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    users,keyword,keyword,pageable);
        if(tasksPage.isEmpty()){
            throw new NotFoundException("You have no task with "+keyword+" keyword");
        }
        List<TasksDTO> tasksDTOList = tasksPage.stream().map(c->TasksDTO.builder()
                .id(c.getId())
                .title(c.getTitle())
                .description(c.getDescription())
                .status(c.getStatus())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .completedAt(c.getCompletedAt())
                .build()).toList();
        return new PageImpl<>(tasksDTOList,pageable,tasksPage.getTotalElements());
    }

    public boolean updateTaskStatus(Long task_id, String status){
        Optional<Tasks> optionalTasks = tasksRepository.findById(task_id);
        if(optionalTasks.isPresent()){
            Tasks task = optionalTasks.get();
            task.setStatus(TaskEnum.valueOf(status.toUpperCase()));
            task.setUpdatedAt(saveLocalDate(LocalDateTime.now()));
            if(status.equalsIgnoreCase("completed")){
                task.setCompletedAt(saveLocalDate(LocalDateTime.now()));
            }else{
                task.setCompletedAt("");
            }
            tasksRepository.save(task);
            return true;
        }
        return false;
    }

    public boolean editTaskTitleAndDescription(Long task_id, String title, String description){
        Optional<Tasks> optionalTasks = tasksRepository.findById(task_id);
        if(optionalTasks.isPresent()){
            Tasks task = optionalTasks.get();
            task.setTitle(title);
            task.setDescription(description);
            task.setUpdatedAt(saveLocalDate(LocalDateTime.now()));
            tasksRepository.save(task);
            return true;
        }
        return false;
    }


    private String saveLocalDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mm:ss a");
        return date.format(formatter);
    }
}
