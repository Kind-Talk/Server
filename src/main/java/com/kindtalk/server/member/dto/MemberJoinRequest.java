package com.kindtalk.server.member.dto;

import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.school.domain.School;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberJoinRequest(
  @NotBlank
  String email,

  @NotBlank
  String password,

  @NotBlank
  String userName,

  @NotBlank
  String nickName,

  String schoolCode,

  @NotNull
  Role role) {

  public Member toEntity(String password, School school) {
    return new Member(
      this.email,
      password,
      this.userName,
      this.nickName,
      school,
      this.role
    );
  }
}
