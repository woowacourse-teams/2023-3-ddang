package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record CreateChatRoomDto(Long auctionId) {

    public ChatRoom toEntity(final Auction auction) {
        final User buyer = auction.findWinner(LocalDateTime.now());

        return new ChatRoom(auction, buyer);
    }
}
