package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.List;

public interface MessageRepository {

    Message save(final Message message);

    // TODO: 2023/10/15 [고민] null 체크 안 하는 로직에서는 원시타입으로 바꿔야할지?
    boolean existsById(final Long lastMessageId);

    List<Message> findAllByLastMessageId(
            final Long messageReaderId,
            final Long chatRoomId,
            final Long lastMessageId
    );
}
