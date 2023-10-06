package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.domain.Bid;

public record ReadAuctionInChatRoomDto(
        Long id,
        String title,
        Integer lastBidPrice,
        Long thumbnailImageId
) {

    public static ReadAuctionInChatRoomDto from(final Auction auction) {
        return new ReadAuctionInChatRoomDto(
                auction.getId(),
                auction.getTitle(),
                convertPrice(auction.getLastBid()),
                auction.getAuctionImages().get(0).getId()
        );
    }

    private static Integer convertPrice(final Bid bid) {
        if (bid == null) {
            return null;
        }

        return bid.getPrice()
                  .getValue();
    }
}
