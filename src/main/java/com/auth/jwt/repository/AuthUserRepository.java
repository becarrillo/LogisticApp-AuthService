package com.auth.jwt.repository;

import com.auth.jwt.entities.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Short> {

    Optional<AuthUser> findByUserName(String userName);
}
