package com.kindtalk.server.security.dto;

import com.kindtalk.server.member.role.Role;

public record LoginResponse(
  String email,
  Role role) {
}
