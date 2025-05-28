package org.example.recipes.repository;

import org.example.recipes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); // 👈 добавили этот метод
    boolean existsByEmail(String email);
}
