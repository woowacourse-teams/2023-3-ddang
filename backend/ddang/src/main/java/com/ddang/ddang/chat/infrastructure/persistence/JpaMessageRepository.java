package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaMessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m join fetch m.chatRoom " +
            "where m.id = (select max(lm.id) from Message lm " +
            "where lm.chatRoom.id = :chatRoomId) order by m.id desc")
    Optional<Message> findLastMessageByChatRoomId(final Long chatRoomId);
}
