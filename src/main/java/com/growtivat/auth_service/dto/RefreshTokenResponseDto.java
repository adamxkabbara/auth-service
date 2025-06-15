package com.growtivat.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}
