package com.growtivat.auth_service.service;

import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.RegisterUserRequestDto;
import com.growtivat.auth_service.dto.AuthenticateUserResponseDto;
import com.growtivat.auth_service.dto.RefreshTokenRequestDto;
import com.growtivat.auth_service.dto.RefreshTokenResponseDto;
import com.growtivat.auth_service.model.RefreshToken;
import com.growtivat.auth_service.model.User;
import com.growtivat.auth_service.repository.UserRepository;
import com.growtivat.auth_service.utils.JwtUtils;
import com.growtivat.auth_service.utils.TokenType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${access.token.ttl}")
    private Long accessTokenTTL;

    @Value("${refresh.token.ttl}")
    private Long refreshTokenTTL;

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

    public ResponseEntity<AuthenticateUserResponseDto> authenticate(
            AuthenticateUserRequestDto authenticateUserRequestDto) {

        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticateUserRequestDto.getUsername(),
                                authenticateUserRequestDto.getPassword()));

        String accessToken = jwtUtils.generateToken(authenticateUserRequestDto.getUsername(), TokenType.ACCESS);
        String refreshToken = jwtUtils.generateToken(authenticateUserRequestDto.getUsername(), TokenType.REFRESH);

        AuthenticateUserResponseDto response = AuthenticateUserResponseDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTokenTTL)
                .build();

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);

    }

    public ResponseEntity<RefreshTokenResponseDto> refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService
                .findByToken(refreshTokenRequestDto.getRefreshToken());

        if (refreshTokenOpt.isEmpty() || isRefreshTokenInvalid(refreshTokenOpt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // If the refresh token is valid, generate a new access token
        String username = jwtUtils.extractUsername(refreshTokenOpt.get().getToken(), TokenType.REFRESH);
        String newAccessToken = jwtUtils.generateToken(username, TokenType.REFRESH);

        RefreshTokenResponseDto response = RefreshTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .expiresIn(refreshTokenTTL)
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> logout(String username) {
        refreshTokenService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    private boolean isRefreshTokenInvalid(Optional<RefreshToken> refreshTokenOpt) {
        String refreshToken = refreshTokenOpt.get().getToken();
        String username = jwtUtils.extractUsername(refreshToken, TokenType.REFRESH);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return !jwtUtils.isValidToken(refreshToken, userDetails, TokenType.REFRESH);
    }
}
