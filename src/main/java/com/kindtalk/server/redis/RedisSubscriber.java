package com.kindtalk.server.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindtalk.server.message.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

  private final ObjectMapper objectMapper;
  private final RedisTemplate<String, Object> redisTemplate;
  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
      MessageResponse response = objectMapper.readValue(publishMessage, MessageResponse.class);

      messagingTemplate.convertAndSend("/sub/chat." + response.roomId(), response);
    } catch (Exception e) {

    }
  }
}
