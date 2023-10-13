package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageDto;

import java.util.List;

public interface MessageRepository {

    List<Message> findMessagesAllByLastMessageId(
            final Long messageReaderId,
            final Long chatRoomId,
            final Long lastMessageId
    );
}
