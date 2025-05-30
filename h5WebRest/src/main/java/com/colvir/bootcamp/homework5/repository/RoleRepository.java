package com.colvir.bootcamp.homework5.repository;

import com.colvir.bootcamp.homework5.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
