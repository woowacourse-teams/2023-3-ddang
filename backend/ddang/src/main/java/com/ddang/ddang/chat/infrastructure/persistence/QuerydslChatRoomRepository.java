package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageDto;

import java.util.List;
import java.util.Optional;

public interface QuerydslChatRoomRepository {

    List<ChatRoomAndMessageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId);

    Optional<ChatRoom> findChatRoomById(final Long chatRoomId);

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);
}
