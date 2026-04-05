package com.kindtalk.server.security.dto;

public record LoginResponse(
  String email,
  String role) {
}
