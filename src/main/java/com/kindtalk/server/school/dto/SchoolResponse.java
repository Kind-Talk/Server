package com.kindtalk.server.school.dto;

import com.kindtalk.server.school.domain.School;

public record SchoolResponse(
  String code,
  String name) {

  public static SchoolResponse of(School school) {
    return new SchoolResponse(
      school.getCode(),
      school.getName()
    );
  }
}
