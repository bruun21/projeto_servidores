package com.servidores.projeto.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.servidores.projeto.model.Role;
import com.servidores.projeto.model.User;
import com.servidores.projeto.repository.RoleRepository;
import com.servidores.projeto.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0 && userRepository.count() == 0) {
            Role role = new Role();
            role.setName("ADMIN");
            role.setDescription("Administradores do sistema");
            roleRepository.save(role);

            User admin = new User();
            admin.setEmail("admin@email.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(role);
            userRepository.save(admin);
            System.out.println("Usuário ADMIN criado com sucesso!");

        }
    }
}
