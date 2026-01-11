package com.kindtalk.server.school.dto;

import jakarta.validation.constraints.NotBlank;

public record SchoolSearchRequest(
  @NotBlank(message = "검색어를 입력해주세요.")
  String search) {
}
