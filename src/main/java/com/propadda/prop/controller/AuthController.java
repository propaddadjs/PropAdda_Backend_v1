package com.propadda.prop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.propadda.prop.config.JwtService;
import com.propadda.prop.dto.AuthResponse;
import com.propadda.prop.dto.ForgotPasswordRequest;
import com.propadda.prop.dto.LoginRequest;
import com.propadda.prop.dto.LoginResponse;
import com.propadda.prop.dto.ResetPasswordRequest;
import com.propadda.prop.dto.SignupRequest;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.UsersRepo;
import com.propadda.prop.security.CustomUserDetailsService;
import com.propadda.prop.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final UsersRepo uRepo;
    private final UserService users;
    private final CustomUserDetailsService uds;
    private final JwtService jwt;

    public AuthController(UsersRepo uRepo, UserService users, CustomUserDetailsService uds, JwtService jwt) {
        this.uRepo = uRepo; this.users = users; this.uds = uds; this.jwt = jwt;
    }

  
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest body) {
        Users u = users.registerBuyer(
                body.firstName(), body.lastName(),
                body.email(), body.phoneNumber(),
                body.state(), body.city(),
                body.password()
        );
        var principal = uds.loadUserByUsername(u.getEmail());
        String access = jwt.generateAccessToken(principal);
        String refresh = jwt.generateRefreshToken(principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapAuth(u, access, refresh));
    }

    private AuthResponse mapAuth(Users u, String access, String refresh) {
        return new AuthResponse(
                access, refresh,
                u.getUserId(), u.getEmail(),
                u.getRole(),
                u.getFirstName(), u.getLastName(),
                u.getKycVerified() != null ? u.getKycVerified().name() : null,
                u.getProfileImageUrl()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(users.login(request));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public AuthResponse me(Authentication auth) {
        Users u = uRepo.findByEmail(auth.getName()).isPresent() ? uRepo.findByEmail(auth.getName()).get() : new Users();
        var principal = uds.loadUserByUsername(u.getEmail());
        String access = jwt.generateAccessToken(principal);
        String refresh = jwt.generateRefreshToken(principal); // optional
        return mapAuth(u, access, refresh);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest req) {
        // Derive app base URL for link
        // String baseUrl = String.format("%s://%s", http.getScheme(), http.getHeader("Host"));
        users.sendResetLink(req.getEmail(), frontendUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest req) {
        if (req.getNewPassword() == null || req.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("message", "Password must be at least 8 characters."));
        }
        users.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
