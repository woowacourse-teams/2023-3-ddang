package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageDto;

import java.util.List;

public interface ChatRoomAndMessageRepository {

    List<ChatRoomAndMessageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId);
}
