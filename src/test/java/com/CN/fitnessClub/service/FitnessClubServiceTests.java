package com.CN.fitnessClub.service;

import com.CN.fitnessClub.dto.FitnessClubDto;
import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.FitnessClubRepository;
import com.CN.fitnessClub.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
@SpringBootTest
@DisplayName("Testing FitnessClubService")
public class FitnessClubServiceTests {
    @Mock
    private FitnessClubRepository fitnessClubRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private FitnessClubService fitnessClubService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing method getAllFitnessClub()")
    public void shouldTestGetAllFitnessClub() {
        List<FitnessClub> fitnessClubs = new ArrayList<>();
        Mockito.when(fitnessClubRepository.findAll()).thenReturn(fitnessClubs);
        List<FitnessClub> resultList = fitnessClubService.getAllFitnessClub();
        Assertions.assertEquals(fitnessClubs.size(), resultList.size());
    }

    @Test
    @DisplayName("Testing method getFitnessClubId()")
    public void shouldTestGetFitnessClubId() {
        Mockito.when(fitnessClubRepository.findById(anyLong())).thenReturn(Optional.of(new FitnessClub()));
        FitnessClub fitnessClub = fitnessClubService.getFitnessClubId(1L);
        Assertions.assertNotNull(fitnessClub);
    }

    @Test
    @DisplayName("Testing method deleteFitnessClubById()")
    public void shouldTestDeleteFitnessClubById() {
        Mockito.when(fitnessClubRepository.existsById(anyLong())).thenReturn(true);
        fitnessClubService.deleteFitnessClubById(1L);
        verify(fitnessClubRepository, Mockito.times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Testing method updateFitnessClub()")
    public void shouldTestUpdateFitnessClub() {
        FitnessClubDto fitnessClubDto = new FitnessClubDto();
        fitnessClubDto.setName("Updated Club");
        fitnessClubDto.setAddress("Updated Address");
        FitnessClub existingFitnessClub = new FitnessClub();
        existingFitnessClub.setId(1L);
        Mockito.when(fitnessClubRepository.findById(anyLong())).thenReturn(Optional.of(existingFitnessClub));
        fitnessClubService.updateFitnessClub(fitnessClubDto, existingFitnessClub.getId());
        verify(fitnessClubRepository, Mockito.times(1)).save(any(FitnessClub.class));
    }

    @Test
    @DisplayName("Testing method createFitnessClub()")
    public void shouldTestCreateFitnessClub() {
        FitnessClubDto fitnessClubDto = new FitnessClubDto();
        fitnessClubDto.setName("New Club");
        fitnessClubService.createFitnessClub(fitnessClubDto);
        verify(fitnessClubRepository, Mockito.times(1)).save(any(FitnessClub.class));
    }

    @Test
    @DisplayName("Testing method addMember()")
    public void shouldTestAddMember() {
        User user = new User();
        user.setId(1L);

        FitnessClub fitnessClub = new FitnessClub();
        fitnessClub.setId(1L);

        Mockito.when(userService.getUserById(anyLong())).thenReturn(user);
        Mockito.when(fitnessClubRepository.findById(anyLong())).thenReturn(Optional.of(fitnessClub));

        fitnessClubService.addMember(user.getId(), fitnessClub.getId());
        verify(fitnessClubRepository, Mockito.times(1)).save(any(FitnessClub.class));
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Testing method deleteMember()")
    public void shouldTestDeleteMember() {
        User user = new User();
        user.setId(1L);

        FitnessClub fitnessClub = new FitnessClub();
        fitnessClub.setId(1L);
        fitnessClub.getMembers().add(user);

        Mockito.when(userService.getUserById(anyLong())).thenReturn(user);
        Mockito.when(fitnessClubRepository.findById(anyLong())).thenReturn(Optional.of(fitnessClub));

        fitnessClubService.deleteMember(user.getId(), fitnessClub.getId());
        verify(fitnessClubRepository, Mockito.times(1)).save(any(FitnessClub.class));
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }
}

