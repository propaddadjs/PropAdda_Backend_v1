package com.propadda.prop.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String role,          // e.g. ADMIN / AGENT / BUYER
        String kycVerified,   // e.g. INAPPLICABLE / PENDING / APPROVED / REJECTED
        Integer userId,
        String firstName,
        String lastName,
        String email
) {}