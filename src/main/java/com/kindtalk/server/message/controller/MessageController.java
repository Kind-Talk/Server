package com.kindtalk.server.message.controller;

import com.kindtalk.server.message.dto.MessageRequest;
import com.kindtalk.server.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @MessageMapping("/chat.{roomId}")
  public void send(
    @DestinationVariable Long roomId,
    @Valid @Payload MessageRequest request
  ) {
    messageService.saveAndSend(roomId, request);
  }
}
