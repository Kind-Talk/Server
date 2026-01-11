package com.kindtalk.server.message.repository;

import com.kindtalk.server.message.document.Message;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  List<Message> findAllByRoomIdOrderBySendAtAsc(Long roomId);
}
