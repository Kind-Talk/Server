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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;
  private final RedisTemplate<String, Object> redisTemplate;

  private static final String CHAT_CACHE_PREFIX = "chat:cache:room:";
  private static final String CHAT_ROOM_PREFIX = "chat:room:";

  public MessageResponse saveAndSend(Long roomId, MessageRequest request) {
    Message message = messageRepository.save(
      new Message(roomId, request.senderId(), request.content(), Instant.now())
    );
    MessageResponse response = MessageResponse.of(message);

    String cacheKey = CHAT_CACHE_PREFIX + roomId;
    redisTemplate.opsForList().leftPush(cacheKey, response);
    redisTemplate.opsForList().trim(cacheKey, 0, 99);

    redisTemplate.convertAndSend(CHAT_ROOM_PREFIX + roomId, response);

    return response;
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
