package com.kindtalk.server.message.document;

import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "chat_message")
@CompoundIndexes({
  @CompoundIndex(name = "idx_room_sendat_desc", def = "{'roomId': 1, 'sendAt': -1}"),
  @CompoundIndex(name = "idx_topic_sendat_asc", def = "{'topicId: 1, 'sendAt': 1, sparse = true")
})
public class Message {

  @Id
  private String id;

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
