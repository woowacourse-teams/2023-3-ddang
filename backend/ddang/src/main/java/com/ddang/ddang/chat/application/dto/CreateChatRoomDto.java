package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record CreateChatRoomDto(Long auctionId) {

    public static CreateChatRoomDto from(final CreateChatRoomRequest chatRoomRequest) {
        return new CreateChatRoomDto(chatRoomRequest.auctionId());
    }

    public ChatRoom toEntity(final Auction auction) {
        final User winner = auction.findWinner(LocalDateTime.now())
                                   .orElseThrow(() -> new WinnerNotFoundException("낙찰자가 존재하지 않습니다"));


        return new ChatRoom(auction, winner);
    }
}
