package com.kindtalk.server.message.controller;

import com.kindtalk.server.message.dto.MessageRequest;
import com.kindtalk.server.message.dto.MessageResponse;
import com.kindtalk.server.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @MessageMapping("/chat.{roomId}")
  @SendTo("/sub/chat.{roomId}")
  public MessageResponse send(
    @DestinationVariable Long roomId,
    @Valid @Payload MessageRequest request
  ) {
    return messageService.saveAndSend(roomId, request);
  }
}
