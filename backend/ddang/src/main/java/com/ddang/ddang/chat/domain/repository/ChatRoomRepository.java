package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;

import java.util.Optional;

public interface ChatRoomRepository {

    boolean existsByAuctionId(final Long auctionId);

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);
}
