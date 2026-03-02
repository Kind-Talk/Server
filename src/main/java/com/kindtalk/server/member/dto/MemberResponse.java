package com.kindtalk.server.member.dto;

import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.school.domain.School;

public record MemberResponse(
  String email,
  String userName,
  String nickName,
  String schoolCode,
  String schoolName,
  Role role) {

  public static MemberResponse of(Member member) {
    School school = member.getSchool();
    boolean isTeacherWithSchool = member.getRole() == Role.TEACHER && school != null;

    String schoolCode = isTeacherWithSchool ? school.getCode() : null;
    String schoolName = isTeacherWithSchool ? school.getName() : null;

    return new MemberResponse(
      member.getEmail(),
      member.getUserName(),
      member.getNickName(),
      schoolCode,
      schoolName,
      member.getRole()
    );
  }
}