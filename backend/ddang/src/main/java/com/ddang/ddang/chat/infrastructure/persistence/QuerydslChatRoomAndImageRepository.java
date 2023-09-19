package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndImageDto;

import java.util.Optional;

public interface QuerydslChatRoomAndImageRepository {

    Optional<ChatRoomAndImageDto> findChatRoomById(final Long chatRoomId);
}
