package com.kindtalk.server.topic.domain;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String title;

  private LocalDateTime archivedAt;

  @ManyToOne
  @JoinColumn(name = "chatroom_id")
  private ChatRoom chatRoom;

  public Topic(String title, ChatRoom chatRoom) {
    this.title = title;
    this.chatRoom = chatRoom;
    archivedAt = LocalDateTime.now();
  }
}
