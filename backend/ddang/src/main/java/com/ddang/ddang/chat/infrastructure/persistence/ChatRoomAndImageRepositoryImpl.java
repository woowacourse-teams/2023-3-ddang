package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndImageDto;
import com.ddang.ddang.chat.domain.repository.ChatRoomAndImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomAndImageRepositoryImpl implements ChatRoomAndImageRepository {

    private final QuerydslChatRoomAndImageRepository querydslChatRoomAndImageRepository;

    @Override
    public Optional<ChatRoomAndImageDto> findChatRoomById(final Long chatRoomId) {
        return querydslChatRoomAndImageRepository.findChatRoomById(chatRoomId);
    }
}
