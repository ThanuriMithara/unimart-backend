package lk.ac.kln.unimartbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String universityEmail,
        @NotBlank String password
) {}