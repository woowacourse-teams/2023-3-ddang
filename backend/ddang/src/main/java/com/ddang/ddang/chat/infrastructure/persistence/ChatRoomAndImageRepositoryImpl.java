package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.domain.dto.ChatRoomAndImageDto;
import com.ddang.ddang.chat.domain.repository.ChatRoomAndImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomAndImageRepositoryImpl implements ChatRoomAndImageRepository {

    private final QuerydslChatRoomAndImageRepository querydslChatRoomAndImageRepository;

    @Override
    public ChatRoomAndImageDto getByIdOrThrow(final Long chatRoomId) {
        return querydslChatRoomAndImageRepository.findChatRoomById(chatRoomId)
                                                 .orElseThrow(() -> new ChatRoomNotFoundException(
                                                         "지정한 아이디에 대한 채팅방을 찾을 수 없습니다."
                                                 ));
    }
}
