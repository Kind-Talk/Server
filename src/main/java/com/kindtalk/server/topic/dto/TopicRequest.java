package com.kindtalk.server.topic.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicRequest(
  @NotBlank(message = "스레드의 제목을 입력해주세요.")
  String title
) {

}
