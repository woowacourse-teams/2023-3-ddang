package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.image.domain.AuctionImage;

import java.time.LocalDateTime;
import java.util.List;

public record ReadAuctionDto(
        Long id,
        String title,
        String description,
        int bidUnit,
        int startPrice,
        Integer lastBidPrice,
        boolean deleted,
        LocalDateTime registerTime,
        LocalDateTime closingTime,
        List<ReadRegionsDto> auctionRegions,
        List<Long> auctionImageIds,
        String mainCategory,
        String subCategory
) {

    public static ReadAuctionDto from(final Auction auction) {
        return new ReadAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getBidUnit().getValue(),
                auction.getStartPrice().getValue(),
                convertPrice(auction.getLastBid()),
                auction.isDeleted(),
                auction.getCreatedTime(),
                auction.getClosingTime(),
                convertReadRegionsDto(auction),
                convertImageUrls(auction),
                auction.getSubCategory().getMainCategory().getName(),
                auction.getSubCategory().getName()
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

    private static List<ReadRegionsDto> convertReadRegionsDto(final Auction auction) {
        return auction.getAuctionRegions()
                      .stream()
                      .map(ReadRegionsDto::from)
                      .toList();
    }
}
