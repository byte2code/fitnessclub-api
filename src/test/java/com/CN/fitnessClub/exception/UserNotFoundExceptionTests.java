package com.CN.fitnessClub.exception;

import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.UserRepository;
import com.CN.fitnessClub.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
@DisplayName("Testing Exception for User")
public class UserNotFoundExceptionTests {
    @MockBean
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("Testing for UserNotFoundException Exception")
    public void shouldTestFitnessClubNotFoundException(){
        Assertions.assertThrows(UserNotFoundException.class,()->userService.getUserById(2L));
    }

    @Test
    @DisplayName("Testing for No Exception case")
    public void shouldNotTestFitnessClubNotFoundException(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Assertions.assertDoesNotThrow(()->userService.getUserById(1L));
    }
}
