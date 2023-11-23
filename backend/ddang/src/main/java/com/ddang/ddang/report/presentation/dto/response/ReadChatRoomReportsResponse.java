package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadChatRoomReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadChatRoomReportsResponse(List<ReadChatRoomReportResponse> reports) {

    public static ReadChatRoomReportsResponse from(final List<ReadChatRoomReportDto> chatRoomReportDtos) {
        final List<ReadChatRoomReportResponse> reportResponses = chatRoomReportDtos.stream()
                                                                                   .map(ReadChatRoomReportResponse::from)
                                                                                   .toList();
        return new ReadChatRoomReportsResponse(reportResponses);
    }

    public record ReadChatRoomReportResponse(
            Long id,

            ReadReporterResponse reporter,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            ReadReportTargetChatRoomSimpleInfoResponse chatRoom,

            String description
    ) {
        private static ReadChatRoomReportResponse from(final ReadChatRoomReportDto chatRoomReportDto) {
            return new ReadChatRoomReportResponse(
                    chatRoomReportDto.id(),
                    new ReadReporterResponse(
                            chatRoomReportDto.reporterDto().id(),
                            chatRoomReportDto.reporterDto().name()
                    ),
                    chatRoomReportDto.createdTime(),
                    new ReadReportTargetChatRoomSimpleInfoResponse(chatRoomReportDto.chatRoomDto().id()),
                    chatRoomReportDto.description()
            );
        }

        public record ReadReporterResponse(Long id, String name) {
        }

        public record ReadReportTargetChatRoomSimpleInfoResponse(Long id) {
        }
    }
}
