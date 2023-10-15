package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface MessageRepository {

    Message save(final Message message);

    boolean existsById(final Long lastMessageId);

    List<Message> findAllByLastMessageId(
            final Long messageReaderId,
            final Long chatRoomId,
            final Long lastMessageId
    );
}
