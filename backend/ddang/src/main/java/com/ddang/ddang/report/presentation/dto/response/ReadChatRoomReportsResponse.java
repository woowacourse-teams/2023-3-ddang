package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;

import java.util.List;

public record ReadChatRoomReportsResponse(List<ReadChatRoomReportResponse> reports) {

    public static ReadChatRoomReportsResponse from(final List<ReadChatRoomReportDto> chatRoomReportDtos) {
        final List<ReadChatRoomReportResponse> reportResponses = chatRoomReportDtos.stream()
                                                                                   .map(ReadChatRoomReportResponse::from)
                                                                                   .toList();
        return new ReadChatRoomReportsResponse(reportResponses);
    }
}
