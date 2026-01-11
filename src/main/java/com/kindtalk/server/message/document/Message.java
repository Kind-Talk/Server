package com.kindtalk.server.message.document;

import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_message")
public class Message {

  @Id
  private Long id;

  private Long roomId;
  private Long senderId;
  private String content;
  private Instant sendAt;

  public Message(Long roomId, Long senderId, String content, Instant sendAt) {
    this.roomId = roomId;
    this.senderId = senderId;
    this.content = content;
    this.sendAt = sendAt;
  }
}
