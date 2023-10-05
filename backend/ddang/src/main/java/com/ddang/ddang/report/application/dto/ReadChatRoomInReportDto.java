package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;

public record ReadChatRoomInReportDto(
        Long id,
        ReadAuctionInReportDto auctionDto,
        ReadUserInReportDto partnerDto
) {

    public static ReadChatRoomInReportDto from(final ChatRoom chatRoom) {
        return new ReadChatRoomInReportDto(
                chatRoom.getId(),
                ReadAuctionInReportDto.from(chatRoom.getAuction()),
                ReadUserInReportDto.from(chatRoom.getBuyer())
        );
    }
}
