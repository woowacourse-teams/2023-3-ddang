package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadChatRoomInReportDto;

public record ReadChatRoomInReportResponse(Long id) {

    public static ReadChatRoomInReportResponse from(final ReadChatRoomInReportDto chatRoomDto) {
        return new ReadChatRoomInReportResponse(chatRoomDto.id());
    }
}
