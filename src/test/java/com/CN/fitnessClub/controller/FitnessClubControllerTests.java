package com.CN.fitnessClub.controller;

import com.CN.fitnessClub.dto.FitnessClubDto;
import com.CN.fitnessClub.jwt.JwtAuthenticationHelper;
import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.FitnessClubRepository;
import com.CN.fitnessClub.service.FitnessClubService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FitnessClubController.class)
@DisplayName("Testing FitnessClubController")
public class FitnessClubControllerTests {
    @MockBean
    private FitnessClubService fitnessClubService;
    @MockBean
    private FitnessClubRepository fitnessClubRepository;
    @MockBean
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach()
    public void setup()
    {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Testing API: 'GET /fitnessClub/all'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestGetAllFitnessClub() throws Exception {
        FitnessClub fitnessClub = new FitnessClub();
        fitnessClub.setId(1L);
        fitnessClub.setName("club 202");
        fitnessClub.setMembershipPlans("Premium");
        fitnessClub.setContactNo(9898989898L);
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        fitnessClub.setMembers(userList);
        Mockito.when(fitnessClubService.getAllFitnessClub()).thenReturn(List.of(fitnessClub));
        mockMvc.perform(get("/fitnessClub/all")
        .contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'GET /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestGetFitnessClubId() throws Exception {
        when(fitnessClubService.getFitnessClubId(anyLong())).thenReturn(new FitnessClub());
        mockMvc.perform(get("/fitnessClub/1")
        .contentType("application/json")).andExpect(status().isOk());
    }

     @Test
     @DisplayName("Testing API: 'POST /fitnessClub/create'")
     @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
     void shouldTestCreateFitnessClub() throws Exception {

         FitnessClubDto fitnessClubDto = new FitnessClubDto();
         fitnessClubDto.setName("Fitness Club");
         fitnessClubDto.setFacilities("new");
         fitnessClubDto.setContactNo(999999L);
         fitnessClubDto.setMembershipPlans("High AI");

         FitnessClub expectedFitnessClub = new FitnessClub();
         expectedFitnessClub.setName("Fitness Club");
         expectedFitnessClub.setFacilities("new");
         expectedFitnessClub.setContactNo(999999L);
         expectedFitnessClub.setMembershipPlans("High AI");
         expectedFitnessClub.setId(1L);

        when(fitnessClubRepository.save(any(FitnessClub.class))).thenReturn(expectedFitnessClub);

        mockMvc.perform(post("/fitnessClub/create")
        .contentType("application/json").content(asJsonString(fitnessClubDto)))
        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testing API: 'PUT /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestUpdateFitnessClub() throws Exception {

        FitnessClubDto fitnessClubDto = new FitnessClubDto();
        fitnessClubDto.setName("Updated Fitness Club");

        FitnessClub expectedFitnessClub = new FitnessClub();
        expectedFitnessClub.setId(1L);
        expectedFitnessClub.setName("Fitness Club");
        expectedFitnessClub.setFacilities("new");
        expectedFitnessClub.setContactNo(999999L);
        expectedFitnessClub.setMembershipPlans("High AI");

        when(fitnessClubRepository.findById(1L)).thenReturn(Optional.of(expectedFitnessClub));
        when(fitnessClubRepository.save(any(FitnessClub.class))).thenReturn(expectedFitnessClub);

        mockMvc.perform(put("/fitnessClub/1")
                .contentType("application/json").content(asJsonString(fitnessClubDto)))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'DELETE /fitnessClub/{id}'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestDeleteFitnessClub() throws Exception {
        mockMvc.perform(delete("/fitnessClub/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Testing API: 'POST /fitnessClub/addMember'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestAddMember() throws Exception {
        mockMvc.perform(post("/fitnessClub/addMember?userId=1&fitnessClubId=1"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Testing API: 'DELETE /fitnessClub/deleteMember'")
    @WithMockUser(username = "john", password = "john123", roles = "ADMIN")
    void shouldTestDeleteMember() throws Exception {
        mockMvc.perform(delete("/fitnessClub/deleteMember?userId=1&fitnessClubId=1"))
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
