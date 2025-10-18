// src/main/java/com/propadda/prop/controller/AuthController.java
package com.propadda.prop.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
import com.propadda.prop.model.RefreshToken;
import com.propadda.prop.model.Users;
import com.propadda.prop.repo.UsersRepo;
import com.propadda.prop.security.CustomUserDetailsService;
import com.propadda.prop.service.RefreshTokenService;
import com.propadda.prop.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    // Cookie config
    @Value("${security.cookie.secure:true}")
    private boolean cookieSecure; // set to false for local dev if needed

    @Value("${security.jwt.refresh-exp-days:60}")
    private long refreshExpDays;

    private final UsersRepo uRepo;
    private final UserService users;
    private final CustomUserDetailsService uds;
    private final JwtService jwt;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UsersRepo uRepo, UserService users, CustomUserDetailsService uds, JwtService jwt, RefreshTokenService refreshTokenService) {
        this.uRepo = uRepo; this.users = users; this.uds = uds; this.jwt = jwt; this.refreshTokenService = refreshTokenService;
    }

    // Reuse your mapAuth helper to return consistent payload
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

        // Persist hashed refresh token
        Instant refreshExpiresAt = Instant.now().plus(refreshExpDays, ChronoUnit.DAYS);
        refreshTokenService.create(u.getUserId(), refresh, refreshExpiresAt);

        // Build cookie
        ResponseCookie cookie = ResponseCookie.from("refresh", refresh)
                .domain(".propadda.in")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(refreshExpDays * 24 * 60 * 60)
                .sameSite("None")
                .build();

        AuthResponse resp = mapAuth(u, access, null); // don't return refresh in body
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        // your users.login might already authenticate & return tokens. We will generate tokens here for clarity.
        LoginResponse loginResp = users.login(request); // assume this returns a LoginResponse with user + tokens or else adjust
        // If your users.login returns tokens, use them; otherwise generate:
        // var principal = uds.loadUserByUsername(loginResp.getEmail());
        // String access = jwt.generateAccessToken(principal);
        // String refresh = jwt.generateRefreshToken(principal);

        // Prefer use loginResp values if present
        String accessToken = loginResp.accessToken();
        String refreshToken = loginResp.refreshToken();

        // Persist hashed refresh
        // Find user id
        Optional<Users> maybeUser = uRepo.findByEmail(loginResp.email());
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid login"));
        }
        Users u = maybeUser.get();

        Instant refreshExpiresAt = Instant.now().plus(refreshExpDays, ChronoUnit.DAYS);
        refreshTokenService.create(u.getUserId(), refreshToken, refreshExpiresAt);

        ResponseCookie cookie = ResponseCookie.from("refresh", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(refreshExpDays * 24 * 60 * 60)
                .sameSite("None")
                .build();

        // Return access + user info. Do NOT return refresh token in body if you prefer
        AuthResponse auth = mapAuth(u, accessToken, null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(auth);
    }

    /**
     * Called by client when they want to rehydrate session.
     * Reads httpOnly refresh cookie, validates & rotates token, issues new access token.
     */
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest req, HttpServletResponse resp) {
        String rawRefresh = extractRefreshCookie(req);
        if (rawRefresh == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No refresh token"));
        }

        // Validate JWT structure & expiry first
        if (!jwt.isTokenValid(rawRefresh)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid refresh token"));
        }

        // Find server-side persisted token
        var maybe = refreshTokenService.findByRaw(rawRefresh);
        if (maybe.isEmpty() || !refreshTokenService.isValid(maybe.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Refresh token invalid or revoked"));
        }

        RefreshToken stored = maybe.get();
        // Extract username from refresh JWT, load user
        String username;
        try {
            username = jwt.extractUsername(rawRefresh);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid refresh token payload"));
        }

        var principal = uds.loadUserByUsername(username);
        // Issue new tokens
        String newAccess = jwt.generateAccessToken(principal);
        String newRefresh = jwt.generateRefreshToken(principal);

        // rotate: mark old revoked and insert new
        Instant newExpiresAt = Instant.now().plus(refreshExpDays, ChronoUnit.DAYS);
        refreshTokenService.rotate(stored, newRefresh, newExpiresAt);

        // Set new cookie
        ResponseCookie cookie = ResponseCookie.from("refresh", newRefresh)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(refreshExpDays * 24 * 60 * 60)
                .sameSite("None")
                .build();
        resp.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Return new access token + minimal user payload
        Optional<Users> maybeUser = uRepo.findByEmail(username);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User not found"));
        }
        Users u = maybeUser.get();
        AuthResponse out = mapAuth(u, newAccess, null);
        return ResponseEntity.ok(out);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public AuthResponse me(Authentication auth) {
        Users u = uRepo.findByEmail(auth.getName()).orElse(new Users());
        var principal = uds.loadUserByUsername(u.getEmail());
        String access = jwt.generateAccessToken(principal);
        // Optionally, you could rotate refresh here too if desired
        return mapAuth(u, access, null);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest req, HttpServletResponse res) {
        String rawRefresh = extractRefreshCookie(req);
        if (rawRefresh != null) {
            var maybe = refreshTokenService.findByRaw(rawRefresh);
            maybe.ifPresent(rt -> refreshTokenService.revoke(rt));
        }

        // delete cookie
        ResponseCookie deleteCookie = ResponseCookie.from("refresh", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }

    private String extractRefreshCookie(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> "refresh".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody ForgotPasswordRequest req) {
        // The UserService already handles sending the email link.
        users.sendResetLink(req.getEmail(), frontendUrl);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest req) {
        if (req.getNewPassword() == null || req.getNewPassword().length() < 8) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Password must be at least 8 characters."));
        }
        users.resetPassword(req.getToken(), req.getNewPassword());
        return ResponseEntity.ok().build();
    }
}
