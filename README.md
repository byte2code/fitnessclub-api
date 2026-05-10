# FitnessClub

Spring Boot application for managing fitness club users, clubs, and workouts with JWT-based security.

## Overview

This project models a club-oriented fitness platform where users can authenticate, join a fitness club, and manage workout plans through REST APIs.

## Features

- User authentication and authorization
- Fitness club management
- Workout management
- MySQL persistence with Spring Data JPA
- Controller, repository, and integration test coverage
- JWT-based authentication flow
- Role-based user access for club members and admins
- Actuator and health endpoint support

## Configuration

- Main application port: `8083`
- Default datasource: MySQL database `fitnessClub`
- Test profile: H2-based in-memory database

## API Areas

- `AuthController` for login and token issuance
- `UserController` for user and membership operations
- `FitnessClubController` for club and workout workflows

## Testing

The repository includes service, repository, controller, and integration tests. The v3 snapshot expands coverage to exercise the app end to end.

## Stack

- Java 17
- Spring Boot 2.7.16
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Lombok
