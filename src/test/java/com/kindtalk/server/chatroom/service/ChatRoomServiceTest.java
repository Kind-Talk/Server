package com.kindtalk.server.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.chatroom.domain.Status;
import com.kindtalk.server.chatroom.dto.ChatRoomCreateRequest;
import com.kindtalk.server.chatroom.dto.ChatRoomResponse;
import com.kindtalk.server.chatroom.dto.UpdateAnnounceRequest;
import com.kindtalk.server.chatroom.repository.ChatRoomRepository;
import com.kindtalk.server.exception.DataNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatRoomServiceTest {

  @Autowired
  private ChatRoomService chatRoomService;

  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @AfterEach
  void tearDown() {
    chatRoomRepository.deleteAll();
  }

  @Test
  void 채팅방_저장_테스트() {
    // given
    ChatRoomCreateRequest request = new ChatRoomCreateRequest("채팅방1", "teacher1");

    // when
    ChatRoomResponse response = chatRoomService.create(request);

    //then
    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response.title()).isEqualTo("채팅방1"),
      () -> assertThat(response.announce()).isNull(),
      () -> assertThat(response.status()).isEqualTo(Status.ACTIVE),
      () -> assertThat(response.teacher()).isEqualTo("teacher1")
    );
  }

  @Test
  void 채팅방_조회_테스트() {
    // given
    ChatRoom mock = chatRoomRepository.save(new ChatRoom("채팅방2", "teacher2"));

    // when
    ChatRoomResponse response = chatRoomService.findById(mock.getId());

    // then
    assertAll(
      () -> assertThat(response).isNotNull(),
      () -> assertThat(response.title()).isEqualTo("채팅방2"),
      () -> assertThat(response.announce()).isNull(),
      () -> assertThat(response.status()).isEqualTo(Status.ACTIVE),
      () -> assertThat(response.teacher()).isEqualTo("teacher2")
    );
  }

  @Test
  void 존재하지_않는_id로_조회시_실패() {
    // given
    ChatRoom mock = chatRoomRepository.save(new ChatRoom("채팅방3", "teacher3"));

    // when && then
    assertThatExceptionOfType(DataNotFoundException.class)
      .isThrownBy(() ->
        chatRoomService.findById(100L)
      )
      .withMessage("해당되는 채팅방이 존재하지 않습니다.");
  }

  @Test
  void 공지사항_수정_테스트() {
    // given
    ChatRoom mock = chatRoomRepository.save(new ChatRoom("채팅방4", "teacher4"));
    UpdateAnnounceRequest request = new UpdateAnnounceRequest(mock.getId(), "공지사항");
    ChatRoomResponse response1 = chatRoomService.findById(request.id());

    // when
    chatRoomService.updateAnnounce(request);
    ChatRoomResponse response2 = chatRoomService.findById(mock.getId());

    // then
    assertAll(
      () -> assertThat(response1).isNotNull(),
      () -> assertThat(response1.announce()).isNull(),
      () -> assertThat(response2).isNotNull(),
      () -> assertThat(response2.title()).isEqualTo("채팅방4"),
      () -> assertThat(response2.teacher()).isEqualTo("teacher4"),
      () -> assertThat(response2.announce()).isEqualTo("공지사항")
    );
  }
}
