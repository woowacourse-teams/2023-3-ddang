package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReadMessageLogRepositoryImpl implements ReadMessageLogRepository {

    private final JpaReadMessageLogRepository jpaReadMessageLogRepository;

    @Override
    public Optional<ReadMessageLog> findBy(final Long readerId, final Long chatRoomId) {
        return jpaReadMessageLogRepository.findLastReadMessageByUserIdAndChatRoomId(readerId, chatRoomId);
    }

    @Override
    public List<ReadMessageLog> saveAll(final List<ReadMessageLog> readMessageLogs) {
        return jpaReadMessageLogRepository.saveAll(readMessageLogs);
    }
}
