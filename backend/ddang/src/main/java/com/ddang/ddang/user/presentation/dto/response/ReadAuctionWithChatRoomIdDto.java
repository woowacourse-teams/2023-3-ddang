package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;

public record ReadAuctionWithChatRoomIdDto(ReadAuctionDto auctionDto, ReadChatRoomDto chatRoomDto) {
}
