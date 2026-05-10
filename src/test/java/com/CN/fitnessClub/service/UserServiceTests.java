package com.CN.fitnessClub.service;

import com.CN.fitnessClub.dto.UserRequest;
import com.CN.fitnessClub.dto.WorkoutDto;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.model.Workout;
import com.CN.fitnessClub.repository.UserRepository;
import com.CN.fitnessClub.repository.WorkoutRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("Testing UserService")
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkoutRepository workoutRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing method getAllUsers()")
    public void shouldTestGetAllUsers() {
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<User> resultList = userService.getAllUsers();
        Assertions.assertEquals(users.size(), resultList.size());
    }

    @Test
    @DisplayName("Testing method createUser()")
    public void shouldTestCreateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("testUser");
        userRequest.setPassword("testPassword");

        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        Mockito.when(bCryptPasswordEncoder.encode(userRequest.getPassword())).thenReturn("encodePassword");

        User expectedUser = new User();
        expectedUser.setEmail("testUser");
        expectedUser.setPassword("encodePassword");

        Mockito.when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        userService.createUser(userRequest);
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Testing method getUserById()")
    public void shouldTestGetUserById() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        User user = userService.getUserById(1L);
        Assertions.assertNotNull(user);
    }

    @Test
    @DisplayName("Testing method updateUser()")
    public void shouldTestUpdateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password");
        userRequest.setAge(30);
        userRequest.setGender("Male");

        BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        Mockito.when(bCryptPasswordEncoder.encode(userRequest.getPassword())).thenReturn("encodePassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("testUser");
        existingUser.setPassword("encodePassword");

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(existingUser);
        userService.updateUser(userRequest,existingUser.getId());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Testing method deleteUser()")
    public void shouldTestDeleteUser() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, Mockito.times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Testing method addWorkout()")
    public void shouldTestAddWorkout() {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setWorkoutName("Running");
        workoutDto.setDuration(30);
        workoutDto.setDescription("Morning Run");
        workoutDto.setDifficultyLevel("Intermediate");

        User existingUser = new User();
        existingUser.setId(1L);

        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        Mockito.when(workoutRepository.save(any(Workout.class))).thenReturn(new Workout());
        userService.addWorkout(workoutDto,existingUser.getId());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
        verify(workoutRepository, Mockito.times(1)).save(any(Workout.class));

    }
}
