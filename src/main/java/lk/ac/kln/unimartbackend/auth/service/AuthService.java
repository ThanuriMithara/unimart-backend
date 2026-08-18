package lk.ac.kln.unimartbackend.auth.service;

import lk.ac.kln.unimartbackend.auth.dto.AuthResponse;
import lk.ac.kln.unimartbackend.auth.dto.LoginRequest;
import lk.ac.kln.unimartbackend.auth.dto.RegisterRequest;
import lk.ac.kln.unimartbackend.auth.entity.User;
import lk.ac.kln.unimartbackend.auth.repository.UserRepository;
import lk.ac.kln.unimartbackend.common.exception.ConflictException;
import lk.ac.kln.unimartbackend.common.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (users.existsByUniversityEmail(request.universityEmail())) {
            throw new ConflictException("An account with this email already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.universityEmail().trim().toLowerCase(),
                hashedPassword,
                request.fullName().trim(),
                request.role()
        );

        User saved = users.save(user);
        String token = jwtService.generateAccessToken(saved);

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getUniversityEmail(),
                saved.getFullName(),
                saved.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = users.findByUniversityEmail(request.universityEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        String token = jwtService.generateAccessToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getUniversityEmail(),
                user.getFullName(),
                user.getRole().name()
        );
    }
}