package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(final ChatRoom chatRoom);

    ChatRoom getSimpleChatRoomByIdOrThrow(final Long id);

    ChatRoom getDetailChatRoomByIdOrThrow(final Long id);

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);

    boolean existsByAuctionId(final Long auctionId);
}
