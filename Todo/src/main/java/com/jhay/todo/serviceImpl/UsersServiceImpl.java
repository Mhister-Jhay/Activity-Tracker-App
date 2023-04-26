package com.jhay.todo.serviceImpl;

import com.jhay.todo.dto.UsersDTO;
import com.jhay.todo.exception.NotFoundException;
import com.jhay.todo.model.Users;
import com.jhay.todo.repository.UsersRepository;
import com.jhay.todo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public boolean getUserRegistered(UsersDTO usersDTO) {
        boolean isUserRegistered = false;
        Optional<Users> optionalUsers = findUserByEmail(usersDTO.getEmail());
        if(optionalUsers.isEmpty()){
            Users users = Users.builder()
                    .firstName(usersDTO.getFirstName())
                    .lastName(usersDTO.getLastName())
                    .email(usersDTO.getEmail())
                    .phoneNumber(usersDTO.getPhoneNumber())
                    .password(usersDTO.getPassword())
                    .build();
            usersRepository.save(users);
            isUserRegistered = true;
        }
        return isUserRegistered;
    }

    @Override
    public UsersDTO getUserLoggedIn(UsersDTO userDTO) {
        Optional<Users> optionalUsers = findUserByEmail(userDTO.getEmail());
        if(optionalUsers.isPresent()){
            Users users = optionalUsers.get();
            if(users.getPassword().equals(userDTO.getPassword())){
                return UsersDTO.builder()
                        .id(users.getId())
                        .firstName(users.getFirstName())
                        .lastName(users.getLastName())
                        .email(users.getEmail())
                        .phoneNumber(users.getPhoneNumber())
                        .password(users.getPassword())
                        .build();
            }else{
                return null;
            }
        } else {
            throw new NotFoundException("User with email: "+userDTO.getEmail()+" not found");
        }
    }

    @Override
    public Optional<Users> findUserByEmail(String email) {
        return usersRepository.findUsersByEmail(email);
    }
}
