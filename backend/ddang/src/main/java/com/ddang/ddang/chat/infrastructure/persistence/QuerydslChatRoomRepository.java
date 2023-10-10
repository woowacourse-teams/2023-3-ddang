package com.ddang.ddang.chat.infrastructure.persistence;

import java.util.Optional;

public interface QuerydslChatRoomRepository {

    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);
}
