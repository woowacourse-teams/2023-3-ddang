package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadChatRoomReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadChatRoomReportResponse(
        Long id,

        ReadReporterResponse reporter,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        ReadChatRoomInReportResponse chatRoom,

        String description
) {
        public static ReadChatRoomReportResponse from(final ReadChatRoomReportDto chatRoomReportDto) {
                return new ReadChatRoomReportResponse(
                        chatRoomReportDto.id(),
                        ReadReporterResponse.from(chatRoomReportDto.reporterDto()),
                        chatRoomReportDto.createdTime(),
                        ReadChatRoomInReportResponse.from(chatRoomReportDto.chatRoomDto()),
                        chatRoomReportDto.description()
                );
        }
}
