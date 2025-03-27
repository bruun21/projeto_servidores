package com.servidores.projeto.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servidores.projeto.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}