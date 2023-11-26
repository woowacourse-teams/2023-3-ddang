package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;

public record ReadSingleAuctionDto(
        Long id,
        String title,
        String description,
        int bidUnit,
        int startPrice,
        Integer lastBidPrice,
        boolean deleted,
        LocalDateTime registerTime,
        LocalDateTime closingTime,
        List<ReadFullDirectRegionDto> auctionRegions,
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

    public static ReadSingleAuctionDto of(final Auction auction, final LocalDateTime targetTime) {
        return new ReadSingleAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getBidUnit().getValue(),
                auction.getStartPrice().getValue(),
                auction.findLastBid().map(lastBid -> lastBid.getPrice().getValue()).orElse(null),
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
                auction.findLastBidder().map(User::getId).orElse(null)
        );
    }

    private static List<String> convertImageStoreNames(final Auction auction) {
        return auction.getAuctionImages()
                      .stream()
                      .map(AuctionImage::getStoreName)
                      .toList();
    }

    private static List<ReadFullDirectRegionDto> convertReadRegionsDto(final Auction auction) {
        return auction.getAuctionRegions()
                      .stream()
                      .map(ReadFullDirectRegionDto::from)
                      .toList();
    }
}
