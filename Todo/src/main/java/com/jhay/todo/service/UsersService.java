package com.jhay.todo.service;

import com.jhay.todo.dto.UsersDTO;
import com.jhay.todo.model.Users;

import java.util.Optional;

public interface UsersService {
    boolean getUserRegistered(UsersDTO storeUsersDTO);
    UsersDTO getUserLoggedIn(UsersDTO storeUserDTO);
    Optional<Users> findUserByEmail(String email);
}
