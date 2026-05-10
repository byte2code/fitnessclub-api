package com.CN.fitnessClub.service;

import com.CN.fitnessClub.dto.UserRequest;
import com.CN.fitnessClub.dto.WorkoutDto;
import com.CN.fitnessClub.exception.UserNotFoundException;
import com.CN.fitnessClub.model.Role;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.model.Workout;
import com.CN.fitnessClub.repository.UserRepository;
import com.CN.fitnessClub.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WorkoutRepository workoutRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(UserRequest userRequest) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userRequest.getPassword());
        User user = User.builder().email(userRequest.getEmail()).age(userRequest.getAge())
                .gender(userRequest.getGender()).password(encodedPassword)
                .build();
        Role role = new Role();
        Set<Role> roles = new HashSet<>();
        if(userRequest.getUserType() != null) {
            if (userRequest.getUserType().equalsIgnoreCase("TRAINER")) {
                role.setRoleName("ROLE_TRAINER");
                roles.add(role);
                user.setRoles(roles);
            } else if (userRequest.getUserType().equalsIgnoreCase("ADMIN")) {
                role.setRoleName("ROLE_ADMIN");
                roles.add(role);
                user.setRoles(roles);
            } else {
                role.setRoleName("ROLE_CUSTOMER");
                roles.add(role);
                user.setRoles(roles);
            }
        }
        else {
            role.setRoleName("ROLE_CUSTOMER");
            roles.add(role);
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public void updateUser(UserRequest userRequest, Long id) {
        User existingUser = getUserById(id);
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));
        existingUser.setAge(userRequest.getAge());
        existingUser.setGender(userRequest.getGender());
        userRepository.save(existingUser);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }



    public void addWorkout(WorkoutDto workoutDto, Long userId) {
        User user = getUserById(userId);
        Workout workout = Workout.builder().workoutName(workoutDto.getWorkoutName())
                .duration(workoutDto.getDuration()).description(workoutDto.getDescription())
                .difficultyLevel(workoutDto.getDifficultyLevel()).user(user).build();
        List<Workout> workouts = user.getWorkouts();
        workouts.add(workout);
        user.setWorkouts(workouts);
        workoutRepository.save(workout);
        userRepository.save(user);
    }

}

