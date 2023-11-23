package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.AuctionStatus;
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
        List<String> auctionImageStoreNames,
        int auctioneerCount,
        String mainCategory,
        String subCategory,
        Long sellerId,
        String sellerProfileStoreName,
        String sellerName,
        Float sellerReliability,
        boolean isSellerDeleted,
        AuctionStatus auctionStatus,
        Long lastBidderId
) {

    public static ReadAuctionDto of(final Auction auction, final LocalDateTime targetTime) {
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
                convertImageStoreNames(auction),
                auction.getAuctioneerCount(),
                auction.getSubCategory().getMainCategory().getName(),
                auction.getSubCategory().getName(),
                auction.getSeller().getId(),
                auction.getSeller().getProfileImageStoreName(),
                auction.getSeller().findName(),
                auction.getSeller().findReliability(),
                auction.getSeller().isDeleted(),
                auction.findAuctionStatus(targetTime),
                convertLastBidderId(auction)
        );
    }

    private static List<String> convertImageStoreNames(final Auction auction) {
        return auction.getAuctionImages()
                      .stream()
                      .map(AuctionImage::getStoreName)
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

    private static Long convertLastBidderId(final Auction auction) {
        if (auction.getLastBid() == null) {
            return null;
        }

        return auction.getLastBid().getBidder().getId();
    }
}
