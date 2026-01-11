package com.kindtalk.server.message.dto;

import com.kindtalk.server.message.document.Message;
import java.time.Instant;

public record MessageResponse(Long roomId, Long senderId, String content, Instant sendAt) {

  public static MessageResponse of(Message message) {
    return new MessageResponse(
      message.getRoomId(),
      message.getSenderId(),
      message.getContent(),
      message.getSendAt()
    );
  }
}
