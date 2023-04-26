package com.jhay.todo.repository;


import com.jhay.todo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findUsersByEmail(String email);
    Users findUsersById(Long id);
}
