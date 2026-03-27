package com.kindtalk.server.topic.dto;

import com.kindtalk.server.topic.domain.Topic;
import java.time.LocalDateTime;

public record TopicResponse(
  Long id,

  String title,

  LocalDateTime archivedAt,

  Long chatRoomId
) {

  public static TopicResponse of(Topic topic) {
    return new TopicResponse(
      topic.getId(),
      topic.getTitle(),
      topic.getArchivedAt(),
      topic.getChatRoom().getId()
    );
  }
}
