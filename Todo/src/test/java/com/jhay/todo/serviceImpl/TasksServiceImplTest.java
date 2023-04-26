package com.jhay.todo.serviceImpl;

import com.jhay.todo.dto.TasksDTO;
import com.jhay.todo.enums.TaskEnum;
import com.jhay.todo.model.Tasks;
import com.jhay.todo.model.Users;
import com.jhay.todo.repository.TasksRepository;
import com.jhay.todo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class TasksServiceImplTest {
    @Mock
    private TasksRepository tasksRepository;
    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private TasksServiceImpl tasksService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createTask() {
        Long user_id = 1L;
        Users users = new Users();
        users.setId(user_id);
        when(usersRepository.findUsersById(user_id)).thenReturn(users);

        Long task_id = 1L;
        String title = "Do something";
        String description = "Do something really bad";
        Tasks tasks = new Tasks();
        tasks.setId(task_id);
        tasks.setTitle(title);
        tasks.setDescription(description);

        TasksDTO tasksDTO = TasksDTO.builder()
                .id(tasks.getId())
                .title(tasks.getTitle())
                .description(tasks.getDescription())
                .build();
        when(tasksRepository.findTasksByUserAndTitle(users,tasksDTO.getTitle())).thenReturn(Optional.empty());
        assertTrue(tasksService.createTask(tasksDTO,user_id));

    }

    @Test
    void viewAllTask() {
        Long user_id = 1L;
        Users users = new Users();
        users.setId(user_id);
        when(usersRepository.findUsersById(user_id)).thenReturn(users);

        Tasks tasks = new Tasks();
        tasks.setId(1L);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");

        int pageNum = 0;
        int pageSize = 1;
        Page<Tasks> tasksPage = new PageImpl<>(Collections.singletonList(tasks));
        when(tasksRepository.findAllByUserId(user_id, PageRequest.of(pageNum,pageSize,Sort.by("updatedAt").descending())))
                .thenReturn(tasksPage);
        Page<TasksDTO> tasksDTOPage = tasksService.viewAllTask(user_id,pageNum,pageSize);
        assertEquals(tasksPage.getTotalElements(), tasksDTOPage.getTotalElements());
        assertEquals(tasksPage.getNumber(), tasksDTOPage.getNumber());
        assertEquals(tasksPage.getSize(), tasksDTOPage.getSize());
        assertEquals(tasksPage.getContent().get(0).getId(), tasksDTOPage.getContent().get(0).getId());
        assertEquals(tasksPage.getContent().get(0).getTitle(), tasksDTOPage.getContent().get(0).getTitle());
        assertEquals(tasksPage.getContent().get(0).getDescription(), tasksDTOPage.getContent().get(0).getDescription());
        assertEquals(tasksPage.getContent().get(0).getStatus(), tasksDTOPage.getContent().get(0).getStatus());
        assertEquals(tasksPage.getContent().get(0).getUpdatedAt(), tasksDTOPage.getContent().get(0).getUpdatedAt());
    }

    @Test
    void viewAllTaskByStatus() {
        Long user_id = 1L;
        String status = "pending";
        int pageNum = 0;
        int pageSize = 1;

        Users users = new Users();
        users.setId(user_id);
        when(usersRepository.findUsersById(user_id)).thenReturn(users);

        Tasks tasks = new Tasks();
        tasks.setId(1L);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");

        Page<Tasks> tasksPage = new PageImpl<>(Collections.singletonList(tasks));
        when(tasksRepository.findAllByUserAndStatus(users, TaskEnum.valueOf(status.toUpperCase()),
                PageRequest.of(pageNum,pageSize,Sort.by("updatedAt").descending())))
                .thenReturn(tasksPage);
        Page<TasksDTO> tasksDTOPage = tasksService.viewAllTaskByStatus(user_id,status,pageNum,pageSize);
        assertEquals(tasksPage.getTotalElements(), tasksDTOPage.getTotalElements());
        assertEquals(tasksPage.getNumber(), tasksDTOPage.getNumber());
        assertEquals(tasksPage.getSize(), tasksDTOPage.getSize());
        assertEquals(tasksPage.getContent().get(0).getId(), tasksDTOPage.getContent().get(0).getId());
        assertEquals(tasksPage.getContent().get(0).getTitle(), tasksDTOPage.getContent().get(0).getTitle());
        assertEquals(tasksPage.getContent().get(0).getDescription(), tasksDTOPage.getContent().get(0).getDescription());
        assertEquals(tasksPage.getContent().get(0).getStatus(), tasksDTOPage.getContent().get(0).getStatus());
        assertEquals(tasksPage.getContent().get(0).getUpdatedAt(), tasksDTOPage.getContent().get(0).getUpdatedAt());
    }

    @Test
    void getTotalTaskByStatus() {
        Long user_id = 1L;
        String status = "pending";
        int pageNum = 0;
        int pageSize = 1;

        Users users = new Users();
        users.setId(user_id);
        when(usersRepository.findUsersById(user_id)).thenReturn(users);

        Tasks tasks = new Tasks();
        tasks.setId(1L);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");

        Page<Tasks> tasksPage = new PageImpl<>(Collections.singletonList(tasks));
        when(tasksRepository.findAllByUserAndStatus(users, TaskEnum.valueOf(status.toUpperCase()),
                PageRequest.of(pageNum,pageSize,Sort.by("updatedAt").descending())))
                .thenReturn(tasksPage);
        Long taskTotal = 1L;
        assertEquals(taskTotal,tasksService.getTotalTaskByStatus(user_id,status,pageNum,pageSize));
    }

    @Test
    void viewSpecificTask() {
        Long task_id = 1L;
        Tasks tasks = new Tasks();
        tasks.setId(task_id);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");
        when(tasksRepository.findById(task_id)).thenReturn(Optional.of(tasks));

        TasksDTO tasksDTO = tasksService.viewSpecificTask(task_id);
        assertEquals(tasks.getId(), tasksDTO.getId());
        assertEquals(tasks.getTitle(), tasksDTO.getTitle());
        assertEquals(tasks.getDescription(), tasksDTO.getDescription());
        assertEquals(tasks.getUpdatedAt(), tasksDTO.getUpdatedAt());
    }

    @Test
    void searchForTask() {
        Long user_id = 1L;
        String keyword = "some";
        int pageNum = 0;
        int pageSize = 1;

        Users users = new Users();
        users.setId(user_id);
        when(usersRepository.findUsersById(user_id)).thenReturn(users);

        Tasks tasks = new Tasks();
        tasks.setId(1L);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");

        Page<Tasks> tasksPage = new PageImpl<>(Collections.singletonList(tasks));
        when(tasksRepository.findAllByUserAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                users, keyword, keyword, PageRequest.of(pageNum,pageSize,Sort.by("updatedAt").descending())))
                .thenReturn(tasksPage);
        Page<TasksDTO> tasksDTOPage = tasksService.searchForTask(user_id,keyword,pageNum,pageSize);
        assertEquals(tasksPage.getTotalElements(), tasksDTOPage.getTotalElements());
        assertEquals(tasksPage.getNumber(), tasksDTOPage.getNumber());
        assertEquals(tasksPage.getSize(), tasksDTOPage.getSize());
        assertEquals(tasksPage.getContent().get(0).getId(), tasksDTOPage.getContent().get(0).getId());
        assertEquals(tasksPage.getContent().get(0).getTitle(), tasksDTOPage.getContent().get(0).getTitle());
        assertEquals(tasksPage.getContent().get(0).getDescription(), tasksDTOPage.getContent().get(0).getDescription());
        assertEquals(tasksPage.getContent().get(0).getStatus(), tasksDTOPage.getContent().get(0).getStatus());
        assertEquals(tasksPage.getContent().get(0).getUpdatedAt(), tasksDTOPage.getContent().get(0).getUpdatedAt());
    }

    @Test
    void updateTaskStatus() {
        Long task_id = 1L;
        Tasks tasks = new Tasks();
        String status = "completed";
        tasks.setId(task_id);
        tasks.setTitle("Do something");
        tasks.setStatus(TaskEnum.PENDING);
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");
        when(tasksRepository.findById(task_id)).thenReturn(Optional.of(tasks));

        assertTrue(tasksService.updateTaskStatus(task_id,status));
        assertEquals(status.toUpperCase(), tasks.getStatus().name());
    }

    @Test
    void editTaskTitleAndDescription() {
        Long task_id = 1L;
        String title = "Do something new";
        String description = "Do something really bad or not";
        Tasks tasks = new Tasks();
        tasks.setId(task_id);
        tasks.setTitle("Do something");
        tasks.setDescription("Do something really bad");
        tasks.setUpdatedAt("14/10/2022 10:24:44 AM");
        when(tasksRepository.findById(task_id)).thenReturn(Optional.of(tasks));
        assertTrue(tasksService.editTaskTitleAndDescription(task_id,title,description));
        assertEquals(title, tasks.getTitle());
        assertEquals(description,tasks.getDescription());
    }
}