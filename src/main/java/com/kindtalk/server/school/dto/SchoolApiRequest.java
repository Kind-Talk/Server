package com.kindtalk.server.school.dto;

import com.kindtalk.server.school.domain.School;

public record SchoolApiRequest(
  String code,
  String name) {

  public School toEntity() {
    return new School(code, name);
  }
}
