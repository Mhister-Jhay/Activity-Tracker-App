package com.jhay.todo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jhay.todo.enums.TaskEnum;
import com.jhay.todo.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TasksDTO {
    private Long id;
    private String title;
    private String description;
    private TaskEnum status;
    private String createdAt;
    private String updatedAt;
    private String completedAt;
    private Users user;
}
