package com.servidores.projeto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.utility.TestcontainersConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    @Order(1)
    void createRole_AdminUserValidRole_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ROLE_TEST\", \"description\":\"Test role description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ROLE_TEST"))
                .andExpect(jsonPath("$.description").value("Test role description"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Order(2)
    void createRole_RoleAlreadyExists_ReturnsConflict() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ROLE_TEST\", \"description\":\"Test role description\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Order(3)
    void createRole_NonAdminUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ROLE_TEST\", \"description\":\"Test role description\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testLogin() {

    }

    @Test
    void testRefreshToken() {

    }

    @Test
    void testRegister() {

    }
}
