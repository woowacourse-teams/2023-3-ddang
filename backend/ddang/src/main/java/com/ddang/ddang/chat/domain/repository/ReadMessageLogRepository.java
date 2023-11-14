package com.ddang.ddang.chat.domain.repository;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import java.util.List;

public interface ReadMessageLogRepository {

    ReadMessageLog findByReaderIdAndChatRoomId(final Long readerId, final Long chatRoomId);

    List<ReadMessageLog> saveAll(List<ReadMessageLog> readMessageLogs);
}
