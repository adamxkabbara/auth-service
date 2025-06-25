package com.growtivat.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateUserRequestDto {
    private String username;
    private String password;
}
