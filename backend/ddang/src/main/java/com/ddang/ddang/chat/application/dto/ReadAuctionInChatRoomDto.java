package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.image.domain.AuctionImage;

import java.util.List;

public record ReadAuctionInChatRoomDto(
        Long id,
        String title,
        Integer lastBidPrice,
        List<Long> auctionImageIds,
        String mainCategory,
        String subCategory,
        Long sellerId,
        Long sellerProfileId,
        String sellerName,
        double sellerReliability
) {

    public static ReadAuctionInChatRoomDto from(final Auction auction) {
        return new ReadAuctionInChatRoomDto(
                auction.getId(),
                auction.getTitle(),
                convertPrice(auction.getLastBid()),
                convertImageUrls(auction),
                auction.getSubCategory().getMainCategory().getName(),
                auction.getSubCategory().getName(),
                auction.getSeller().getId(),
                auction.getSeller().getProfileImage().getId(),
                auction.getSeller().getName(),
                auction.getSeller().getReliability()
        );
    }

    private static List<Long> convertImageUrls(final Auction auction) {
        return auction.getAuctionImages()
                      .stream()
                      .map(AuctionImage::getId)
                      .toList();
    }

    private static Integer convertPrice(final Bid bid) {
        if (bid == null) {
            return null;
        }

        return bid.getPrice()
                  .getValue();
    }
}
