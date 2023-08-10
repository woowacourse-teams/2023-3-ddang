package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;

import java.util.Optional;

public interface QuerydslMessageRepository {

    Optional<Message> findLastMessageByChatRoomId(final Long chatRoomId);
}
