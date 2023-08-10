package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;

import java.time.LocalDateTime;

public record ReadChatRoomInReportDto(
        Long id,
        ReadAuctionInReportDto auctionDto,
        ReadUserInReportDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadChatRoomInReportDto from(final ChatRoom chatRoom, final LocalDateTime targetTime) {
        return new ReadChatRoomInReportDto(
                chatRoom.getId(),
                ReadAuctionInReportDto.from(chatRoom.getAuction()),
                ReadUserInReportDto.from(chatRoom.getBuyer()),
                chatRoom.isChatAvailableTime(targetTime)
        );
    }
}
