package com.datacrowd.backend.user.dto;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        String role
) {
}
