package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;

import java.util.List;

public interface QuerydslMessageRepository {

    List<Message> findMessagesAllByLastMessageId(
            final Long readUserId,
            final Long chatRoomId,
            final Long lastMessageId);
}
