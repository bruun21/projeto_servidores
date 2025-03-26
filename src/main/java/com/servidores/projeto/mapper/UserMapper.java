package com.servidores.projeto.mapper;


import org.springframework.stereotype.Component;

import com.servidores.projeto.dto.UserResponse;
import com.servidores.projeto.model.User;

@Component
public class UserMapper {
    
    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}