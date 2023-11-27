package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadChatRoomReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleChatRoomReportResponse(List<ReadChatRoomReportResponse> reports) {

    public static ReadMultipleChatRoomReportResponse from(final List<ReadChatRoomReportDto> chatRoomReportDtos) {
        final List<ReadChatRoomReportResponse> reportResponses = chatRoomReportDtos.stream()
                                                                                   .map(ReadChatRoomReportResponse::from)
                                                                                   .toList();
        return new ReadMultipleChatRoomReportResponse(reportResponses);
    }

    public record ReadChatRoomReportResponse(
            Long id,
            ReporterInfoResponse reporter,
            LocalDateTime createdTime,
            ReportedChatRoomInfoResponse chatRoom,
            String description
    ) {
        private static ReadChatRoomReportResponse from(final ReadChatRoomReportDto chatRoomReportDto) {
            return new ReadChatRoomReportResponse(
                    chatRoomReportDto.id(),
                    new ReporterInfoResponse(
                            chatRoomReportDto.reporterDto().id(),
                            chatRoomReportDto.reporterDto().name()
                    ),
                    chatRoomReportDto.createdTime(),
                    new ReportedChatRoomInfoResponse(chatRoomReportDto.chatRoomDto().id()),
                    chatRoomReportDto.description()
            );
        }

        public record ReporterInfoResponse(Long id, String name) {
        }

        public record ReportedChatRoomInfoResponse(Long id) {
        }
    }
}
