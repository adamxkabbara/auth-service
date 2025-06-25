package com.growtivat.auth_service.service;

import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.NewUserRequestDto;
import com.growtivat.auth_service.dto.AuthenticateUserResponseDto;
import com.growtivat.auth_service.model.RefreshToken;
import com.growtivat.auth_service.model.User;
import com.growtivat.auth_service.repository.UserRepository;
import com.growtivat.auth_service.utils.JwtUtils;
import com.growtivat.auth_service.utils.TokenType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    public ResponseEntity<Void> register(NewUserRequestDto newUserRequestDto) {
        User user = User
                .builder()
                .username(newUserRequestDto.getUsername())
                .email(newUserRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(newUserRequestDto.getPassword()))
                .firstname(newUserRequestDto.getFirstname())
                .lastname(newUserRequestDto.getLastname())
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

        RefreshToken refreshTokenOjb = RefreshToken.builder()
            .token(refreshToken)
            .username(authenticateUserRequestDto.getUsername())
            .build();
        refreshTokenService.save(refreshTokenOjb);

        AuthenticateUserResponseDto response = AuthenticateUserResponseDto
                .builder()
                .accessToken(accessToken)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenTTL)
                .sameSite("Strict")
                .build();


        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    public ResponseEntity<AuthenticateUserResponseDto> createNewAccessTokenFrom(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Check if the refresh token is valid
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService
                .findByToken(refreshToken);

        if (refreshTokenOpt.isEmpty() || isRefreshTokenInvalid(refreshTokenOpt.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // If the refresh token is valid, generate a new access token
        String username = jwtUtils.extractUsername(refreshTokenOpt.get().getToken(), TokenType.REFRESH);
        String newAccessToken = jwtUtils.generateToken(username, TokenType.ACCESS);

        AuthenticateUserResponseDto response = AuthenticateUserResponseDto.builder()
                .accessToken(newAccessToken)
                .expiresIn(accessTokenTTL)
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Void> logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            System.out.println(refreshToken);
            refreshTokenService.deleteByToken(refreshToken);
        }
        return ResponseEntity.ok().build();
    }

    private boolean isRefreshTokenInvalid(RefreshToken refreshTokenOpt) {
        String refreshToken = refreshTokenOpt.getToken();
        String username = jwtUtils.extractUsername(refreshToken, TokenType.REFRESH);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return !jwtUtils.isValidToken(refreshToken, userDetails, TokenType.REFRESH);
    }
}
