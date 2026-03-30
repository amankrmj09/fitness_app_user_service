# User Service

A simple Spring Boot microservice for managing user accounts in a fitness application ecosystem.

## Features
- User registration and deletion
- User authentication (JWT-based)
- User profile management
- Role-based access (USER, ADMIN)

## Main Endpoints
- `GET /users/all` — List all users
- `POST /users/register` — Register a new user
- `DELETE /users/delete?uuid=...` — Delete a user by UUID

## Main Classes
- `UserServiceApplication` — Main entry point
- `UserController` — REST API endpoints
- `UserService` — Business logic for user operations
- `UserRepository` — JPA repository for users
- `User` — User entity (fields: id, email, password, firstName, lastName, phoneNumber, roles, etc.)
- `UserDTO` — Data transfer object for user info
- `Role` — Enum for user roles (ROLE_USER, ROLE_ADMIN)

## Security
- JWT authentication
- Passwords hashed with Argon2
- Role-based authorization

## Configuration
- Main config: `src/main/resources/application.yaml`
- Runs on port 8070 by default

## Build & Run
```sh
./gradlew build
./gradlew bootRun
```
