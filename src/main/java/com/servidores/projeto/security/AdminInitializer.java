package com.servidores.projeto.security;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.servidores.projeto.security.model.Role;
import com.servidores.projeto.security.model.User;
import com.servidores.projeto.security.repository.RoleRepository;
import com.servidores.projeto.security.repository.UserRepository;

import jakarta.transaction.Transactional;
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
    @Transactional
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
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(roles);
        userRepository.save(admin);
    }
}