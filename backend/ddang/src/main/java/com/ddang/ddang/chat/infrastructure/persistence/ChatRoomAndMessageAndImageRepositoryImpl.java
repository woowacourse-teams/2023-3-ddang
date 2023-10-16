package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.chat.domain.repository.ChatRoomAndMessageAndImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomAndMessageAndImageRepositoryImpl implements ChatRoomAndMessageAndImageRepository {

    private final QuerydslChatRoomAndMessageAndImageRepository querydslChatRoomAndMessageAndImageRepository;

    @Override
    public List<ChatRoomAndMessageAndImageDto> findAllChatRoomInfoByUserIdOrderByLastMessage(final Long userId) {
        return querydslChatRoomAndMessageAndImageRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(userId);
    }
}
