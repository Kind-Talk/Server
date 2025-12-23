package com.kindtalk.server;

import static org.assertj.core.api.Assertions.assertThat;

import com.kindtalk.server.chatroom.domain.Status;
import com.kindtalk.server.chatroom.dto.ChatRoomCreateRequest;
import com.kindtalk.server.chatroom.dto.ChatRoomResponse;
import com.kindtalk.server.chatroom.service.ChatRoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatRoomTest {

  @Autowired
  private ChatRoomService chatRoomService;

  @Test
  public void 채팅방_저장_테스트() {
    // given
    ChatRoomCreateRequest request = new ChatRoomCreateRequest("채팅방1", "teacher1");

    // when
    ChatRoomResponse response = chatRoomService.create(request);

    //then
    assertThat(response).isNotNull();
    assertThat(response.title()).isEqualTo("채팅방1");
    assertThat(response.announce()).isNull();
    assertThat(response.status()).isEqualTo(Status.ACTIVE);
    assertThat(response.teacher()).isEqualTo("teacher1");
  }
}
