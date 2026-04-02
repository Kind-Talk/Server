package com.kindtalk.server.member.domain;

import com.kindtalk.server.member.role.Role;
import com.kindtalk.server.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String password;
  private String userName;
  private String nickName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "school_code")
  private School school;

  @Enumerated(EnumType.STRING)
  private Role role;

  public Member(String email, String password, String userName, String nickName, School school, Role role) {
    this.email = email;
    this.password = password;
    this.userName = userName;
    this.nickName = nickName;
    this.school = school;
    this.role = role;
  }

  public void updateInfo(String userName, String nickName) {
    this.userName = userName;
    this.nickName = nickName;
  }

  public void updateSchool(School school) {
    this.school = school;
  }
}
