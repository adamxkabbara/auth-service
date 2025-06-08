package com.growtivat.auth_service.controller;

import com.growtivat.auth_service.dto.AuthenticateUserRequestDto;
import com.growtivat.auth_service.dto.RegisterUserRequestDto;
import com.growtivat.auth_service.dto.AuthenticateUserResponseDto;
import com.growtivat.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return authService.register(registerUserRequestDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateUserResponseDto> authenticate(@RequestBody AuthenticateUserRequestDto authenticateUserRequestDto) {
        return authService.authenticate(authenticateUserRequestDto);
    }
}