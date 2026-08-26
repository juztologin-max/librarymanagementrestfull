package com.hms.api.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<LoginUser, Long> {
    Optional<LoginUser> findByUsername(String name);

    boolean existsByUsername(String username);
}
