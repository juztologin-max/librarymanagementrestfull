package com.hms.api.login;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.api.login.UserRoles.UserRolesEnum;

public interface UserRolesRepo extends JpaRepository<UserRoles, Long> {
    @Override
    public Optional<UserRoles> findById(Long id);

    public boolean existsByRole(UserRolesEnum role);

    public Optional<UserRoles> getByRole(UserRolesEnum role);
}
