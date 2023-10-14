package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public ChatRoom save(final ChatRoom chatRoom) {
        return jpaChatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(final Long id) {
        return jpaChatRoomRepository.findById(id);
    }

    @Override
    public Optional<Long> findChatRoomIdByAuctionId(final Long auctionId) {
        return jpaChatRoomRepository.findChatRoomIdByAuctionId(auctionId);
    }

    @Override
    public boolean existsByAuctionId(final Long auctionId) {
        return jpaChatRoomRepository.existsByAuctionId(auctionId);
    }
}
