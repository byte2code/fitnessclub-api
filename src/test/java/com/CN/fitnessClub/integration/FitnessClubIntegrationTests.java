package com.CN.fitnessClub.integration;

import com.CN.fitnessClub.dto.FitnessClubDto;
import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.FitnessClubRepository;
import com.CN.fitnessClub.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Integration Testing for FitnessClubController")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FitnessClubIntegrationTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FitnessClubRepository fitnessClubRepository;

    @Autowired
    UserRepository userRepository;

    FitnessClub savedClub = new FitnessClub();
    User savedUser = new User();

    @BeforeEach
    void setup(){
        FitnessClub fitnessClub = new FitnessClub();
        fitnessClub.setId(1L);
        fitnessClub.setName("club 202");
        fitnessClub.setMembershipPlans("Premium");
        fitnessClub.setContactNo(9898989898L);
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        fitnessClub.setMembers(userList);
        savedClub = fitnessClubRepository.save(fitnessClub);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("testPassword");
        user.setGender("Tester");
        user.setAge(88);
        savedUser = userRepository.save(user);
    }
    @AfterEach
    void dsetup(){
        fitnessClubRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    @Order(1)
    @DisplayName("Testing API: 'POST /fitnessClub/create'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestCreateFitnessClub() throws Exception {

        FitnessClubDto fitnessClubDto = new FitnessClubDto();
        fitnessClubDto.setName("club 202");
        fitnessClubDto.setMembershipPlans("Premium");
        fitnessClubDto.setContactNo(9898989898L);
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        fitnessClubDto.setMembers(userList);

        mockMvc.perform(post("/fitnessClub/create")
                        .contentType("application/json").content(asJsonString(fitnessClubDto)))
                .andExpect(status().isCreated());
    }


    @Test
    @Order(2)
    @DisplayName("Testing API: 'GET /fitnessClub/all'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestGetAllFitnessClub() throws Exception {
        mockMvc.perform(get("/fitnessClub/all")
                .contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("Testing API: 'GET /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestGetFitnessClubId() throws Exception {
        mockMvc.perform(get("/fitnessClub/"+savedClub.getId())
                .contentType("application/json")).andExpect(status().isOk());
    }


    @Test
    @Order(4)
    @DisplayName("Testing API: 'PUT /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestUpdateFitnessClub() throws Exception {

        FitnessClubDto fitnessClubDto = new FitnessClubDto();
        fitnessClubDto.setName("Updated Fitness Club");
        fitnessClubDto.setAddress("Updated Address");

        mockMvc.perform(put("/fitnessClub/"+savedClub.getId())
                        .contentType("application/json").content(asJsonString(fitnessClubDto)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Testing API: 'POST /fitnessClub/addMember'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestAddMember() throws Exception {
        mockMvc.perform(post("/fitnessClub/addMember?userId="+savedUser.getId()+"&fitnessClubId="+savedClub.getId()))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(6)
    @DisplayName("Testing API: 'DELETE /fitnessClub/deleteMember'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestDeleteMember() throws Exception {
        mockMvc.perform(delete("/fitnessClub/deleteMember?userId="+savedUser.getId()+"&fitnessClubId="+savedClub.getId()))
                .andExpect(status().isOk());
    }


    @Test
    @Order(7)
    @DisplayName("Testing API: 'DELETE /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestDeleteFitnessClub() throws Exception {
        mockMvc.perform(delete("/fitnessClub/"+savedClub.getId()))
                .andExpect(status().isOk());
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
