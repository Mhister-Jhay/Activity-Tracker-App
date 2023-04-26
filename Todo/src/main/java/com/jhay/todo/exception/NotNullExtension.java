package com.jhay.todo.exception;

public class NotNullExtension extends RuntimeException{
    public NotNullExtension(String message) {
        super(message);
    }
}
