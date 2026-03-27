package com.kindtalk.server.message.service;

import com.kindtalk.server.message.document.Message;
import com.kindtalk.server.message.dto.MessageRequest;
import com.kindtalk.server.message.dto.MessageResponse;
import com.kindtalk.server.message.repository.MessageRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

  public Slice<MessageResponse> getHistory(Long roomId, Instant cursor, int size) {
    Pageable pageable = PageRequest.of(0, size);

    if (cursor == null) {
      cursor = Instant.now();
    }

    return messageRepository.findByRoomIdAndSendAtLessThanOrderBySendAtDesc(
        roomId,
        cursor,
        pageable)
      .map(MessageResponse::of);
  }

  public Slice<MessageResponse> getHistoryOfTopic(Long topicId, Instant cursor, int size) {
    Pageable pageable = PageRequest.of(0, size);

    if (cursor == null) {
      cursor = Instant.now();
    }

    return messageRepository.findByTopicIdAndSendAtLessThanOrderBySendAtAsc(
        topicId,
        cursor,
        pageable)
      .map(MessageResponse::of);
  }
}
