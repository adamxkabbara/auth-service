package com.growtivat.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateUserResponseDto {
    private String accessToken;
    private int expiresIn;
}
