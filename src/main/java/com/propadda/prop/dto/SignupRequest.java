// Author-Hemant Arora
package com.propadda.prop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @Pattern(regexp="\\d{10}", message="Phone must be 10 digits") String phoneNumber,
        @NotBlank String state,
        @NotBlank String city,
        @Size(min=8, message="Password must be at least 8 chars") String password
) {}
