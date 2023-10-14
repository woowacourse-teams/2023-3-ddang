package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;

import java.util.List;

public interface ChatRoomAndMessageAndImageRepository {

    List<ChatRoomAndMessageAndImageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId);
}
