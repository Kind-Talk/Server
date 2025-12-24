package com.kindtalk.server.config.security.dto;

import org.springframework.http.HttpStatus;

public record LogoutResponse(
    HttpStatus status,
    String message) {
}
