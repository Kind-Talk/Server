package com.kindtalk.server.config.security.dto;

import com.kindtalk.server.member.role.Role;

public record LoginResponse(
    String email,
    Role role) {
}
