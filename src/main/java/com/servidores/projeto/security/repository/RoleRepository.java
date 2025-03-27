package com.servidores.projeto.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servidores.projeto.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String email);

    boolean existsByName(String name);

}
