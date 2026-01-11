package com.kindtalk.server.message.dto;

public record MessageRequest(Long roomId, Long senderId, String content) {
  
}
