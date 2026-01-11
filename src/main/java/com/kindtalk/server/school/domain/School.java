package com.kindtalk.server.school.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class School {

  @Id
  @Column(unique = true)
  private String code;
  private String name;

  public School(String code, String name) {
    this.code = code;
    this.name = name;
  }
}
