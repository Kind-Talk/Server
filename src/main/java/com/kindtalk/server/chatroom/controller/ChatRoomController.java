package com.kindtalk.server.chatroom.controller;

import com.kindtalk.server.chatroom.dto.ChatRoomCreateRequest;
import com.kindtalk.server.chatroom.dto.ChatRoomResponse;
import com.kindtalk.server.chatroom.dto.UpdateAnnounceRequest;
import com.kindtalk.server.chatroom.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping
  public ResponseEntity<ChatRoomResponse> create(
    @Valid @RequestBody ChatRoomCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
      .body(chatRoomService.create(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ChatRoomResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(chatRoomService.findById(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ChatRoomResponse> update(
    @PathVariable Long id,
    @RequestBody UpdateAnnounceRequest request) {
    return ResponseEntity.ok(chatRoomService.updateAnnounce(id, request));
  }
}
