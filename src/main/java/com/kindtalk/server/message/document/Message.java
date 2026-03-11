package com.kindtalk.server.message.document;

import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_message")
public class Message {

  @Id
  private String id;

  @Indexed
  private Long roomId;
  private Long senderId;
  private String content;
  private Instant sendAt;
  private Long topicId;
  private Long parentId;

  public Message(Long roomId, Long senderId, String content, Instant sendAt) {
    this.roomId = roomId;
    this.senderId = senderId;
    this.content = content;
    this.sendAt = sendAt;
  }

  public Message(Long roomId, Long senderId, String content, Instant sendAt, Long parentId) {
    this(roomId, senderId, content, sendAt);
    this.parentId = parentId;
  }

  public void setTopicId(Long topicId) {
    this.topicId = topicId;
  }
}
