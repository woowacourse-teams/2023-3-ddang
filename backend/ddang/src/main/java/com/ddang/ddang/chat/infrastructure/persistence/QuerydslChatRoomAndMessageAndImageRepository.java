package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageAndImageDto;

import java.util.List;

public interface QuerydslChatRoomAndMessageAndImageRepository {

    List<ChatRoomAndMessageAndImageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId);
}
