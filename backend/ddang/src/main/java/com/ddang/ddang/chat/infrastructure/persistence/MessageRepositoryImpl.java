package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

    private final JpaMessageRepository jpaMessageRepository;
    private final QuerydslMessageRepository querydslMessageRepository;

    @Override
    public Message save(final Message message) {
        return jpaMessageRepository.save(message);
    }

    @Override
    public boolean existsById(final Long lastMessageId) {
        return jpaMessageRepository.existsById(lastMessageId);
    }

    @Override
    public List<Message> findAllByLastMessageId(
            final Long messageReaderId,
            final Long chatRoomId,
            final Long lastMessageId
    ) {
        return querydslMessageRepository.findAllByLastMessageId(messageReaderId, chatRoomId, lastMessageId);
    }
}
