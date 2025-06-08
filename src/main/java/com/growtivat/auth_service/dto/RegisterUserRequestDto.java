package com.growtivat.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequestDto {
    String username;
    String email;
    String password;
    String firstname;
    String lastname;
}
