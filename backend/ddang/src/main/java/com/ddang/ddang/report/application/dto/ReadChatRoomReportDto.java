package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.report.domain.ChatRoomReport;

import java.time.LocalDateTime;

public record ReadChatRoomReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadChatRoomInReportDto chatRoomDto,
        String description
) {

    public static ReadChatRoomReportDto from(final ChatRoomReport chatRoomReport) {
        return new ReadChatRoomReportDto(
                chatRoomReport.getId(),
                ReadReporterDto.from(chatRoomReport.getReporter()),
                chatRoomReport.getCreatedTime(),
                ReadChatRoomInReportDto.from(chatRoomReport.getChatRoom()),
                chatRoomReport.getDescription()
        );
    }
}
