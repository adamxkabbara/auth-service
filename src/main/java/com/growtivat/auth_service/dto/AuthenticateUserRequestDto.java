package com.growtivat.auth_service.dto;

import lombok.Data;

@Data
public class AuthenticateUserRequestDto {
    private String username;
    private String password;
}
