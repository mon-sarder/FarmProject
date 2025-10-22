package com.example.queueinv;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record ItemDTO(@NotBlank String name, @Min(0) int qty) {}
public record AddOrIncDTO(@NotBlank String name, @Min(1) int amount) {}
public record SetQtyDTO(@NotBlank String name, @Min(0) int qty) {}

public record CustomerDTO(@NotBlank String name, String note) {}

record RegisterDTO(
        @NotBlank
        @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
        String username,

        @NotBlank
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).*$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, and one special character (@$!%*?&)"
        )
        String password
) {}

/**
 * DTO for user login.
 */
record LoginDTO(
        @NotBlank String username,
        @NotBlank String password
) {}

/**
 * DUP for security responses (e.g., login success)
 */
record AuthResponseDTO(
        String username,
        String message
) {}
