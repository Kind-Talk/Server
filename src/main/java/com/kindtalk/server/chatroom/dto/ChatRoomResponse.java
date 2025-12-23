package com.kindtalk.server.chatroom.dto;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.chatroom.domain.Status;

public record ChatRoomResponse(
  String title,
  String announce,
  String teacher,
  Status status
) {

  public static ChatRoomResponse of(ChatRoom chatRoom) {
    return new ChatRoomResponse(
      chatRoom.getTitle(),
      chatRoom.getAnnounce(),
      chatRoom.getTeacher(),
      chatRoom.getStatus()
    );
  }
}
