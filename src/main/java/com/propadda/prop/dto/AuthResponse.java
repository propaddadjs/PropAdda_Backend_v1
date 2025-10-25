// Author-Hemant Arora
package com.propadda.prop.dto;

import com.propadda.prop.enumerations.Role;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        Integer userId,
        String email,
        Role role,
        String firstName,
        String lastName,
        String kycVerified,
        String profileImageUrl
) {}