package com.atlas.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atlas.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}