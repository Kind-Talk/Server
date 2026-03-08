package com.kindtalk.server.topic.controller;

import com.kindtalk.server.message.dto.MessageResponse;
import com.kindtalk.server.message.service.MessageService;
import com.kindtalk.server.topic.dto.TopicRequest;
import com.kindtalk.server.topic.dto.TopicResponse;
import com.kindtalk.server.topic.service.TopicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

  private final TopicService topicService;
  private final MessageService messageService;

  @GetMapping("/{id}")
  public ResponseEntity<TopicResponse> getTopic(@PathVariable Long id) {
    return ResponseEntity.ok(topicService.getTopic(id));
  }

  @GetMapping("/chatroom/{id}")
  public ResponseEntity<List<TopicResponse>> getTopics(
    @PathVariable("id") Long chatRoomId
  ) {
    return ResponseEntity.ok(topicService.getTopics(chatRoomId));
  }

  @PostMapping("/chatroom/{id}")
  public ResponseEntity<TopicResponse> createTopics(
    @PathVariable("id") Long chatRoomId,
    @RequestBody TopicRequest topicRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(topicService.createTopic(chatRoomId, topicRequest));
  }

  @GetMapping("/{id}/messages")
  public ResponseEntity<List<MessageResponse>> getHistoryOfTopic(
    @PathVariable("id") Long topicId
  ) {
    return ResponseEntity.ok(messageService.getHistoryOfTopic(topicId));
  }
}
