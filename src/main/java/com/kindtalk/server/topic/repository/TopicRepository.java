package com.kindtalk.server.topic.repository;

import com.kindtalk.server.chatroom.domain.ChatRoom;
import com.kindtalk.server.topic.domain.Topic;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.aggregation.SelectionOperators.Top;

public interface TopicRepository extends JpaRepository<Topic, Long> {

  List<Topic> findAllByChatRoom(ChatRoom chatRoom);
}
