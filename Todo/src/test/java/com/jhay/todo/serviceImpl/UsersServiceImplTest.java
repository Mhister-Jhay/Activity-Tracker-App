package com.jhay.todo.serviceImpl;

import com.jhay.todo.dto.UsersDTO;
import com.jhay.todo.model.Users;
import com.jhay.todo.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UsersServiceImplTest {
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserRegistered() {
        String email = "kenny@gmail.com";
        UsersDTO users = new UsersDTO();
        users.setEmail(email);
        when(usersRepository.findUsersByEmail(email)).thenReturn(Optional.empty());
        assertTrue(usersService.getUserRegistered(users));
    }

    @Test
    void getUserLoggedIn() {
        String email = "kenny@gmail.com";
        String password = "1234";
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(password);

        UsersDTO usersDTO1 = UsersDTO.builder().email(email).password(password).build();

        when(usersRepository.findUsersByEmail(email)).thenReturn(Optional.of(users));
        UsersDTO usersDTO = usersService.getUserLoggedIn(usersDTO1);

        assertNotNull(usersDTO);
        assertEquals(users.getEmail(), usersDTO.getEmail());
    }

    @Test
    void findUserByEmail() {
        String email = "kenny@gmail.com";
        Users users = new Users();
        users.setEmail(email);
        when(usersRepository.findUsersByEmail(email)).thenReturn(Optional.of(users));
        assertEquals(Optional.of(users),usersService.findUserByEmail(email));
    }
}