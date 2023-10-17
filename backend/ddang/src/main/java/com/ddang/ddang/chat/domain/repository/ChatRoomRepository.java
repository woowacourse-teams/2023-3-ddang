package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(final ChatRoom chatRoom);

    Optional<ChatRoom> findById(final Long id);

    Optional<ChatRoom> findChatRoomByAuctionId(final Long auctionId);

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);

    boolean existsByAuctionId(final Long auctionId);
}
