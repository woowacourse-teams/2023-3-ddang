package com.ddang.ddang.report.domain.repository;

import com.ddang.ddang.report.domain.ChatRoomReport;

import java.util.List;

public interface ChatRoomReportRepository {

    ChatRoomReport save(final ChatRoomReport chatRoomReport);

    boolean existsByChatRoomIdAndReporterId(final Long chatRoomId, final Long reporterId);

    List<ChatRoomReport> findAll();
}
