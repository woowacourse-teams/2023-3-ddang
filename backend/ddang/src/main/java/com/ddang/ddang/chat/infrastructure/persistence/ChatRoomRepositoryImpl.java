package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;
    private final QuerydslChatRoomRepository querydslChatRoomRepository;

    @Override
    public boolean existsByAuctionId(final Long auctionId) {
        return jpaChatRoomRepository.existsByAuctionId(auctionId);
    }

    @Override
    public Optional<Long> findChatRoomIdByAuctionId(final Long auctionId) {
        return querydslChatRoomRepository.findChatRoomIdByAuctionId(auctionId);
    }
}
