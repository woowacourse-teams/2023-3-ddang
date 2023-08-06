package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c where c.auction.seller.id = :userId or c.buyer.id = :userId")
    List<ChatRoom> findAllByUserId(final Long userId);
}
