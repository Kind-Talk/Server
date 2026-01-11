package com.kindtalk.server.message.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
  @NotBlank
  Long senderId,

  @NotBlank
  String content
) {

}
