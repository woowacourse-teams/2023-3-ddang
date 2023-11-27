package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.chat.domain.repository.MultipleChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MultipleChatRoomInfoRepositoryImpl implements MultipleChatRoomRepository {

    private final QuerydslMultipleChatRoomInfoRepository querydslMultipleChatRoomInfoRepository;

    @Override
    public List<MultipleChatRoomInfoDto> findAllByUserIdOrderByLastMessage(final Long userId) {
        return querydslMultipleChatRoomInfoRepository.findAllChatRoomInfoByUserIdOrderByLastMessage(userId);
    }
}
