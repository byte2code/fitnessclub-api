package com.CN.fitnessClub.integration;

import com.CN.fitnessClub.dto.UserRequest;
import com.CN.fitnessClub.dto.WorkoutDto;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration Testing for UserController")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    User savedUser = new User();

    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("test-db1")
            .withUsername("testUser1")
            .withPassword("password1");

    @BeforeAll
    static void beforeAll(){
        MY_SQL_CONTAINER.start();
    }


    @AfterAll
    static void AfterAll(){
        MY_SQL_CONTAINER.stop();
    }

    @BeforeEach
    void setup(){
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("testPassword");
        user.setGender("Tester");
        user.setAge(88);
        savedUser = userRepository.save(user);
    }

    @Test
    @Order(1)
    @DisplayName("Test GET /user/all")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("Test POST /user/register")
    void testRegisterUser() throws Exception {
        UserRequest userRequest = new UserRequest("test@example.com", "password123", 25, "Male", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    @DisplayName("Test GET /user/{id}")
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/"+savedUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Test PUT /user/{id}")
    @WithMockUser(username = "customer", roles = "CUSTOMER")
    void testUpdateUser() throws Exception {
        UserRequest userRequest = new UserRequest("updated@example.com", "updatedPassword", 30, "Female", "Customer");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/"+savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Test POST /user/workout/{userId}")
    @WithMockUser(username = "trainer", roles = "TRAINER")
    void testAddWorkout() throws Exception {
        WorkoutDto workoutDto = new WorkoutDto("Running", "Cardio exercise", "Intermediate", 45);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/workout/"+savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(workoutDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(6)
    @DisplayName("Test DELETE /user/{id}")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/"+savedUser.getId(), 1L))
                .andExpect(status().isOk());
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
