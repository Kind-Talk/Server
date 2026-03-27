package com.kindtalk.server.message.repository;

import com.kindtalk.server.message.document.Message;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  Slice<Message> findByTopicIdAndSendAtLessThanOrderBySendAtAsc(
    Long topicId,
    Instant sendAt,
    Pageable pageable);

  Slice<Message> findByRoomIdAndSendAtLessThanOrderBySendAtDesc(
    Long roomId,
    Instant sendAt,
    Pageable pageable);
}
