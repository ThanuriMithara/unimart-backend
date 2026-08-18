package lk.ac.kln.unimartbackend.auth.dto;

public record AuthResponse(
        String accessToken,
        Long userId,
        String universityEmail,
        String fullName,
        String role
) {}