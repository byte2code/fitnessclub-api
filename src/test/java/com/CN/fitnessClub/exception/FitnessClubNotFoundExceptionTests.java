package com.CN.fitnessClub.exception;

import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.repository.FitnessClubRepository;
import com.CN.fitnessClub.service.FitnessClubService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
@DisplayName("Testing Exception for FitnessClub")
public class FitnessClubNotFoundExceptionTests {
    @MockBean
    FitnessClubRepository fitnessClubRepository;
    @Autowired
    FitnessClubService fitnessClubService;

    @Test
    @DisplayName("Testing for FitnessClubNotFound Exception")
    public void shouldTestFitnessClubNotFoundException(){
        Assertions.assertThrows(FitnessClubNotFoundException.class,()->fitnessClubService.getFitnessClubId(2L));
    }

    @Test
    @DisplayName("Testing for No Exception case")
    public void shouldNotTestFitnessClubNotFoundException(){
        Mockito.when(fitnessClubRepository.findById(1L)).thenReturn(Optional.of(new FitnessClub()));
        Assertions.assertDoesNotThrow(()->fitnessClubService.getFitnessClubId(1L));
    }

}
