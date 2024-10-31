package com.auth.jwt.services;

import com.auth.jwt.dto.AuthUserDto;
import com.auth.jwt.entities.AuthUser;
import com.auth.jwt.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;


    public AuthService(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser save(AuthUserDto dto) {
        Optional<AuthUser> authUserOptional = authUserRepository.findByUserName(dto.getUserName());

        if (authUserOptional.isPresent()) {
            return null;
        }

        final AuthUser authUser = AuthUser.builder()
                .userName(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        return authUserRepository.save(authUser);
    }
}
