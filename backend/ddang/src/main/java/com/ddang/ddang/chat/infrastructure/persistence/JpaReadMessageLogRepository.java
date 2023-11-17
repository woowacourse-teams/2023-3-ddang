package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaReadMessageLogRepository extends JpaRepository<ReadMessageLog, Long> {

    @Query("""
        SELECT rml
        FROM ReadMessageLog rml 
        WHERE rml.chatRoom.id = :chatRoomId AND rml.reader.id = :readerId
    """)
    Optional<ReadMessageLog> findLastReadMessageByUserIdAndChatRoomId(final Long readerId, final Long chatRoomId);
}
