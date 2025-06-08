package com.growtivat.auth_service.service;

import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.RegisterUserRequestDto;
import com.growtivat.auth_service.dto.AuthenticateUserResponseDto;
import com.growtivat.auth_service.model.User;
import com.growtivat.auth_service.repository.UserRepository;
import com.growtivat.auth_service.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${token.ttl}")
    private int tokenTTL;

    public ResponseEntity<Void> register(RegisterUserRequestDto registerUserRequestDto) {
        User user = User
                .builder()
                .username(registerUserRequestDto.getUsername())
                .email(registerUserRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(registerUserRequestDto.getPassword()))
                .firstname(registerUserRequestDto.getFirstname())
                .lastname(registerUserRequestDto.getLastname())
                .build();
        userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    public ResponseEntity<AuthenticateUserResponseDto> authenticate(AuthenticateUserRequestDto authenticateUserRequestDto) {

        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticateUserRequestDto.getUsername(),
                                authenticateUserRequestDto.getPassword()
                        )
                );

        AuthenticateUserResponseDto response = AuthenticateUserResponseDto
                .builder()
                .accessToken(jwtUtils.generateJwt(authenticateUserRequestDto.getUsername()))
                .expiresIn(tokenTTL)
                .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);

    }
}
