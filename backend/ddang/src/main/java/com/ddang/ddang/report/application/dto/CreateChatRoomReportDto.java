package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.user.domain.User;

public record CreateChatRoomReportDto(Long chatRoomId, String description, Long reporterId) {

    public ChatRoomReport toEntity(final User reporter, final ChatRoom chatRoom) {
        return new ChatRoomReport(reporter, chatRoom, description);
    }
}
