package com.servidores.projeto.mapper;

import org.springframework.stereotype.Component;

import com.servidores.projeto.dto.RoleDto;
import com.servidores.projeto.dto.UserResponse;
import com.servidores.projeto.model.Role;
import com.servidores.projeto.model.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                toRoleDto(user.getRole()),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public RoleDto toRoleDto(Role role) {
        return new RoleDto(role.getName(), role.getDescription());
    }
}
