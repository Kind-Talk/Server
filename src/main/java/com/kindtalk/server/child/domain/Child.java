package com.kindtalk.server.child.domain;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.member.domain.Member;
import com.kindtalk.server.school.domain.School;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Child {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "school_code", nullable = false)
  private School school;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chatroom_id")
  private ChatRoom chatRoom;

  public Child(String name, Member member, School school) {
    this.name = name;
    this.member = member;
    this.school = school;
  }

  public void updateInfo(String name, School school) {
    this.name = name;
    this.school = school;
  }

  public void updateChatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
  }
}
