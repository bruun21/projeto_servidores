package com.servidores.projeto.security.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.servidores.projeto.security.dto.RoleDto;
import com.servidores.projeto.security.dto.UserResponse;
import com.servidores.projeto.security.model.Role;
import com.servidores.projeto.security.model.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRoles().stream().map(this::toRoleDto).collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public RoleDto toRoleDto(Role role) {
        return new RoleDto(role.getName(), role.getDescription());
    }
}
