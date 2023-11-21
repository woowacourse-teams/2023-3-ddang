package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.image.domain.AuctionImage;

public record ReadAuctionInChatRoomDto(
        Long id,
        String title,
        Integer lastBidPrice,
        String thumbnailImageStoreName
) {

    public static ReadAuctionInChatRoomDto of(final Auction auction, final AuctionImage thumbnailImage) {
        return new ReadAuctionInChatRoomDto(
                auction.getId(),
                auction.getTitle(),
                convertPrice(auction.getLastBid()),
                thumbnailImage.getStoreName()
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
