package com.CN.fitnessClub.repository;

import com.CN.fitnessClub.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testing method UserRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;
    private User savedUser;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("testPassword");
        user.setGender("Tester");
        user.setAge(88);
        savedUser = userRepository.save(user);
    }

    @Test
    @Order(1)
    @DisplayName("Testing method getUserById()")
    public void shouldTestGetUserById() {
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        Assertions.assertThat(retrievedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(Optional.of(savedUser));
    }

    @Test
    @Order(2)
    @DisplayName("Testing method getAllUsers()")
    public void shouldTestGetAllUsers() {
        List<User> userList = userRepository.findAll();
        Assertions.assertThat(userList)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedUser));
    }

    @Test
    @Order(3)
    @DisplayName("Testing method updateUser()")
    public void shouldTestUpdateUser() {
        savedUser.setEmail("updated@example.com");
        savedUser.setPassword("updatedPassword");
        User updatedUser = userRepository.save(savedUser);
        Assertions.assertThat(updatedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(savedUser);
    }

    @Test
    @Order(4)
    @DisplayName("Testing method deleteUser()")
    public void shouldTestDeleteUser() {
        userRepository.deleteById(savedUser.getId());
        Assertions.assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    @Order(5)
    @DisplayName("Testing method createUser()")
    public void shouldTestCreateUser() {
        User userRequest = new User();
        userRequest.setEmail("newuser@example.com");
        userRequest.setPassword("newUserPassword");
        User savedUserNew = userRepository.save(userRequest);
        Assertions.assertThat(savedUserNew)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userRequest);
    }


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
}
