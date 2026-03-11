package com.kindtalk.server.message.service;

import com.kindtalk.server.message.document.Message;
import com.kindtalk.server.message.dto.MessageRequest;
import com.kindtalk.server.message.dto.MessageResponse;
import com.kindtalk.server.message.repository.MessageRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  public MessageResponse saveAndSend(Long roomId, MessageRequest request) {
    Message message = messageRepository.save(
      new Message(roomId, request.senderId(), request.content(), Instant.now())
    );

    return MessageResponse.of(message);
  }

  public List<MessageResponse> getHistory(Long roomId) {
    return messageRepository.findAllByRoomIdOrderBySendAtAsc(roomId).stream()
      .map(MessageResponse::of)
      .toList();
  }

  public List<MessageResponse> getHistoryOfTopic(Long topicId) {
    return messageRepository.findAllByTopicIdOrderBySendAtDesc(topicId).stream()
      .map(MessageResponse::of)
      .toList();
  }
}
