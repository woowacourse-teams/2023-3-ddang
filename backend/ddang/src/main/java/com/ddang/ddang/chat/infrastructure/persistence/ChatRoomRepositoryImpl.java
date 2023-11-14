package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final JpaChatRoomRepository jpaChatRoomRepository;

    @Override
    public ChatRoom save(final ChatRoom chatRoom) {
        return jpaChatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoom getByIdOrThrow(final Long id) {
        return jpaChatRoomRepository.findById(id)
                                    .orElseThrow(() -> new ChatRoomNotFoundException(
                                            "지정한 채팅방을 찾을 수 없습니다."
                                    ));
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
