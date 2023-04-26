package com.jhay.todo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler
    public ModelAndView notFoundHandler(NotFoundException ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .referer(request.getHeader("referer"))
                .build();
        mav.setViewName("error");
        mav.addObject("errorResponse", errorResponse);
        return mav;
    }

    @ExceptionHandler
    public ModelAndView notNullHandler(NotNullExtension ex,
                                                         HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .localDateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .referer(request.getHeader("referer"))
                .build();
        mav.setViewName("error");
        mav.addObject("errorResponse", errorResponse);
        return mav;
    }
}
