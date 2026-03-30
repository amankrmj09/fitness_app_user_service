package org.fitness.user_service.repository;

import org.fitness.user_service.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String usernameOrEmail);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
