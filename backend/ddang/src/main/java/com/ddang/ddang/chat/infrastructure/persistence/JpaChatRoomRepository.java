package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
        SELECT c.id
        FROM ChatRoom c
        WHERE c.auction.id = :auctionId
    """)
    Optional<Long> findChatRoomIdByAuctionId(final Long auctionId);

    boolean existsByAuctionId(final Long auctionId);
}
