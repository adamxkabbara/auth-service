package com.growtivat.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUserRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstname;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastname;
}
