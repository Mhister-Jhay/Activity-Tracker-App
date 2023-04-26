package com.jhay.todo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorResponse {
    @JsonFormat(pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime localDateTime;
    private int statusCode;
    private String path;
    private String message;
    private String referer;
}
