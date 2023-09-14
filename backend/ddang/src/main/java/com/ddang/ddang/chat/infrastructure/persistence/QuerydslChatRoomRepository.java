package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;

import java.util.Optional;

public interface QuerydslChatRoomRepository {

    Optional<ChatRoom> findChatRoomById(final Long chatRoomId);

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);
}
