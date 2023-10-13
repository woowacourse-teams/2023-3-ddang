package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final JpaMessageRepository jpaMessageRepository;
    private final QuerydslMessageRepository querydslMessageRepository;

    @Override
    public List<Message> findMessagesAllByLastMessageId(final Long messageReaderId, final Long chatRoomId, final Long lastMessageId) {
        return querydslMessageRepository.findMessagesAllByLastMessageId(messageReaderId, chatRoomId, lastMessageId);
    }
}
