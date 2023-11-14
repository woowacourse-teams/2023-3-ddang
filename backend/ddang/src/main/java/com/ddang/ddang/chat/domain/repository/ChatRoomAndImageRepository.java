package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndImageDto;

public interface ChatRoomAndImageRepository {

    ChatRoomAndImageDto getByIdOrThrow(final Long chatRoomId);
}
