package com.CN.fitnessClub.controller;

import com.CN.fitnessClub.dto.JwtRequest;
import com.CN.fitnessClub.dto.JwtResponse;
import com.CN.fitnessClub.jwt.JwtAuthenticationHelper;
import com.CN.fitnessClub.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthenticationHelper jwtAuthenticationHelper;

    @Test
    @DisplayName("Test API: POST '/auth/login'")
    void testLogin() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testUser");
        jwtRequest.setPassword("testPassword");

        String request = objectMapper.writeValueAsString(jwtRequest);

        JwtResponse jwtResponse = new JwtResponse("token");

        Mockito.when(authService.login(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/auth/login").contentType("application/json")
        .content(request)).andExpect(status().isOk());
    }

}
