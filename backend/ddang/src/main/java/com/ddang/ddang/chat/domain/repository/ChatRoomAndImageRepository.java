package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ChatRoomAndImageDto;

import java.util.Optional;

public interface ChatRoomAndImageRepository {

    Optional<ChatRoomAndImageDto> findChatRoomById(final Long chatRoomId);
}
