package com.kindtalk.server.chatroom.service;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.chatroom.dto.ChatRoomCreateRequest;
import com.kindtalk.server.chatroom.dto.ChatRoomResponse;
import com.kindtalk.server.chatroom.dto.UpdateAnnounceRequest;
import com.kindtalk.server.chatroom.repository.ChatRoomRepository;
import com.kindtalk.server.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  @Transactional
  public ChatRoomResponse create(ChatRoomCreateRequest request) {
    ChatRoom chatRoom = new ChatRoom(request.title(), request.teacher());

    return ChatRoomResponse.of(chatRoomRepository.save(chatRoom));
  }

  @Transactional(readOnly = true)
  public ChatRoomResponse findById(Long id) {
    ChatRoom chatRoom = chatRoomRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("해당되는 채팅방이 존재하지 않습니다."));

    return ChatRoomResponse.of(chatRoom);
  }

  @Transactional
  public ChatRoomResponse updateAnnounce(UpdateAnnounceRequest request) {
    ChatRoom chatRoom = chatRoomRepository.findById(request.id())
      .orElseThrow(() -> new DataNotFoundException("해당되는 채팅방이 존재하지 않습니다."));

    chatRoom.setAnnounce(request.announce());

    return ChatRoomResponse.of(chatRoom);
  }
}
