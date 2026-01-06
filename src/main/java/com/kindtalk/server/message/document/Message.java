package com.kindtalk.server.chat.document;

import jakarta.persistence.Id;
import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_message")
public class Message {

  @Id
  private String id;

  private Long roomId;
  private String senderId;
  private String content;
  private Instant sendAt;
}
