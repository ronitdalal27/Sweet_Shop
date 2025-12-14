package com.example.sweetshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data

public class AuthRequest {
    @NotBlank(message = "username required")
    private String username;

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "password required")
    private String password;

    private String role;
}
