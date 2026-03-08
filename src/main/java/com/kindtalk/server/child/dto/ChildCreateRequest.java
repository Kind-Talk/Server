package com.kindtalk.server.child.dto;

import jakarta.validation.constraints.NotBlank;

public record ChildCreateRequest(
  @NotBlank
  String name,

  @NotBlank
  String schoolCode) {
}
