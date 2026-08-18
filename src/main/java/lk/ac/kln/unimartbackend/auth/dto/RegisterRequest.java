package lk.ac.kln.unimartbackend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lk.ac.kln.unimartbackend.auth.entity.Role;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 190) String universityEmail,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotBlank @Size(max = 120) String fullName,
        @NotNull Role role
) {}
