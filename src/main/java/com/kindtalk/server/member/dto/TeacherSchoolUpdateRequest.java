package com.kindtalk.server.member.dto;

import jakarta.validation.constraints.NotBlank;

public record TeacherSchoolUpdateRequest(
  @NotBlank
  String schoolCode) {
}
