package com.kindtalk.server.topic.service;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.chatroom.repository.ChatRoomRepository;
import com.kindtalk.server.exception.DataNotFoundException;
import com.kindtalk.server.topic.domain.Topic;
import com.kindtalk.server.topic.dto.TopicRequest;
import com.kindtalk.server.topic.dto.TopicResponse;
import com.kindtalk.server.topic.repository.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopicService {

  private final TopicRepository topicRepository;
  private final ChatRoomRepository chatRoomRepository;

  @Transactional(readOnly = true)
  public List<TopicResponse> getTopics(Long chatRoomId) {
    ChatRoom chatRoom = getChatRoom(chatRoomId);

    return topicRepository.findByChatRoom(chatRoom).stream()
      .map(TopicResponse::of)
      .toList();
  }

  @Transactional
  public TopicResponse createTopic(Long chatRoomId, TopicRequest topicRequest) {
    ChatRoom chatRoom = getChatRoom(chatRoomId);

    Topic topic = new Topic(topicRequest.title(), chatRoom);

    return TopicResponse.of(topicRepository.save(topic));
  }

  @Transactional(readOnly = true)
  public TopicResponse getTopic(Long id) {
    return TopicResponse.of(
      topicRepository.findById(id)
        .orElseThrow(() -> new DataNotFoundException("존재하지 않는 스레드입니다."))
    );
  }

  private ChatRoom getChatRoom(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId).orElseThrow(
      () -> new DataNotFoundException("존재하지 않는 채팅방입니다.")
    );
  }
}
