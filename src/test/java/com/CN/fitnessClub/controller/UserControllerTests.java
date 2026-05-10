package com.CN.fitnessClub.controller;

import com.CN.fitnessClub.dto.UserRequest;
import com.CN.fitnessClub.dto.WorkoutDto;
import com.CN.fitnessClub.jwt.JwtAuthenticationHelper;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@DisplayName("Testing UserController")
public class UserControllerTests {
    @MockBean
    private UserService userService;
    @MockBean
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Testing API: 'GET /user/all'")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldTestGetAllUsers() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user1");

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/user/all")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'POST /user/register'")
    void shouldTestRegisterUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("testUser");
        userRequest.setPassword("testPassword");

        mockMvc.perform(post("/user/register")
                        .contentType("application/json")
                        .content(asJsonString(userRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testing API: 'GET /user/{id}'")
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void shouldTestGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("testUser");

        when(userService.getUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/user/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'PUT /user/{id}'")
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void shouldTestUpdateUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("updatedUser");

        mockMvc.perform(put("/user/1")
                        .contentType("application/json")
                        .content(asJsonString(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'DELETE /user/{id}'")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldTestDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'POST /user/workout/{userId}'")
    @WithMockUser(username = "trainer", roles = "TRAINER")
    void shouldTestAddWorkout() throws Exception {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setWorkoutName("Workout details");

        mockMvc.perform(post("/user/workout/1")
                        .contentType("application/json")
                        .content(asJsonString(workoutDto)))
                .andExpect(status().isCreated());
    }

    // Utility method to convert objects to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
