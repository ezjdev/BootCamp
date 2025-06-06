package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
