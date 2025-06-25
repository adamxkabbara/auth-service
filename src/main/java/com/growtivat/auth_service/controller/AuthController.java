package com.growtivat.auth_service.controller;

import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.NewUserRequestDto;
import com.growtivat.auth_service.dto.AuthenticateUserResponseDto;
import com.growtivat.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid NewUserRequestDto newUserRequestDto) {
        return authService.register(newUserRequestDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateUserResponseDto> authenticate(
            @RequestBody AuthenticateUserRequestDto authenticateUserRequestDto) {
        return authService.authenticate(authenticateUserRequestDto);
    }

    @GetMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        return authService.createNewAccessTokenFrom(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        return authService.logout(refreshToken);
    }
}