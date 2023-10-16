package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ReadMessageLog;

import java.util.Optional;

public interface ReadMessageLogRepository {

    ReadMessageLog save(final ReadMessageLog readMessageLog);

    Optional<ReadMessageLog> findLastReadMessageBy(final Long readerId, final Long chatRoomId);
}
