package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReadMessageLogRepositoryImpl implements ReadMessageLogRepository {

    private final JpaReadMessageLogRepository jpaReadMessageLogRepository;

    @Override
    public ReadMessageLog save(final ReadMessageLog readMessageLog) {
        return jpaReadMessageLogRepository.save(readMessageLog);
    }

    @Override
    public Optional<ReadMessageLog> findLastReadMessageBy(final Long readerId, final Long chatRoomId) {
        return jpaReadMessageLogRepository.findLastReadMessageByUserIdAndChatRoomId(readerId, chatRoomId);
    }
}
