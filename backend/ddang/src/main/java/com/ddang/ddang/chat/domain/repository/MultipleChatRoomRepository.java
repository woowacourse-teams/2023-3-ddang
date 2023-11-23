package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;

import java.util.List;

public interface MultipleChatRoomRepository {

    List<MultipleChatRoomInfoDto> findAllByUserIdOrderByLastMessage(final Long userId);
}
