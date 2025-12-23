package com.kindtalk.server.chatroom.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomCreateRequest(
  @NotBlank
  String title,

  @NotBlank
  String teacher
) {

}
