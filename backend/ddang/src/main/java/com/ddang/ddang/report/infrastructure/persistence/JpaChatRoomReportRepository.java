package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.ChatRoomReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaChatRoomReportRepository extends JpaRepository<ChatRoomReport, Long> {

    boolean existsByChatRoomIdAndReporterId(final Long chatRoomId, final Long reporterId);

    @Query("""
        select crr
        from ChatRoomReport crr
        join fetch crr.reporter
        join fetch crr.chatRoom cr
        join fetch cr.buyer
        join fetch cr.auction a
        join fetch a.seller
        order by crr.id asc
    """)
    List<ChatRoomReport> findAll();
}
