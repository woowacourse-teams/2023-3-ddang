package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ReadMessageLog;

import java.util.List;
import java.util.Optional;

public interface ReadMessageLogRepository {

    Optional<ReadMessageLog> findBy(final Long readerId, final Long chatRoomId);

    List<ReadMessageLog> saveAll(List<ReadMessageLog> readMessageLogs);
}
