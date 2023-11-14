package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.application.exception.ReadMessageLogNotFoundException;
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadMessageLogRepositoryImpl implements ReadMessageLogRepository {

    private final JpaReadMessageLogRepository jpaReadMessageLogRepository;

    @Override
    public ReadMessageLog findByReaderIdAndChatRoomId(final Long readerId, final Long chatRoomId) {
        return jpaReadMessageLogRepository.findLastReadMessageByUserIdAndChatRoomId(readerId, chatRoomId)
                                          .orElseThrow(() ->
                                                  new ReadMessageLogNotFoundException(
                                                          "메시지 조회 로그가 존재하지 않습니다."
                                                  )
                                          );
    }

    @Override
    public List<ReadMessageLog> saveAll(final List<ReadMessageLog> readMessageLogs) {
        return jpaReadMessageLogRepository.saveAll(readMessageLogs);
    }
}
