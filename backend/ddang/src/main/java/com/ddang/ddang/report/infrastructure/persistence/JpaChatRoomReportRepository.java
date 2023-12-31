package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.ChatRoomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaChatRoomReportRepository extends JpaRepository<ChatRoomReport, Long> {

    boolean existsByChatRoomIdAndReporterId(final Long chatRoomId, final Long reporterId);

    @Query("""
        SELECT crr
        FROM ChatRoomReport crr
        JOIN FETCH crr.reporter
        JOIN FETCH crr.chatRoom cr
        JOIN FETCH cr.buyer
        JOIN FETCH cr.auction a
        JOIN FETCH a.seller
        ORDER BY crr.id ASC
    """)
    List<ChatRoomReport> findAll();
}
