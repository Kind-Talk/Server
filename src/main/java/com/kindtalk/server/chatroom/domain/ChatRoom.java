package com.kindtalk.server.chatroom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String announce;

  private String teacher;

  @Enumerated(EnumType.STRING)
  private Status status;

  public ChatRoom(String title, String teacher) {
    this.title = title;
    this.announce = null;
    this.teacher = teacher;
    this.status = Status.ACTIVE;
  }

  public void setAnnounce(String announce) {
    this.announce = announce;
  }
}
