package com.servidores.projeto.security;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.role.name}")
    private String adminRoleName;

    @Value("${admin.role.description}")
    private String adminRoleDescription;

    @Override
    public void run(String... args) throws Exception {
        if (shouldCreateAdmin()) {
            Role role = createAdminRole();
            createAdminUser(role);
            System.out.println("Usuário ADMIN criado com sucesso!");
        }
    }

    private boolean shouldCreateAdmin() {
        return roleRepository.count() == 0 && userRepository.count() == 0;
    }

    private Role createAdminRole() {
        Role role = new Role();
        role.setName(adminRoleName);
        role.setDescription(adminRoleDescription);
        return roleRepository.save(role);
    }

    private void createAdminUser(Role role) {
        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(role);
        userRepository.save(admin);
    }
}