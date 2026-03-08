package com.kindtalk.server.child.dto;

import com.kindtalk.server.child.domain.Child;

public record ChildResponse(
  Long id,
  Long parentId,
  String name,
  String schoolCode,
  String schoolName) {

  public static ChildResponse of(Child child) {
    return new ChildResponse(
      child.getId(),
      child.getMember().getId(),
      child.getName(),
      child.getSchool().getCode(),
      child.getSchool().getName()
    );
  }
}