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
        int auctioneerCount,
        String mainCategory,
        String subCategory,
        Long sellerId,
        Long sellerProfileId,
        String sellerName,
        double sellerReliability
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
                convertImageIds(auction),
                auction.getAuctioneerCount(),
                auction.getSubCategory().getMainCategory().getName(),
                auction.getSubCategory().getName(),
                auction.getSeller().getId(),
                auction.getSeller().getProfileImage().getId(),
                auction.getSeller().getName(),
                auction.getSeller().getReliability()
        );
    }

    // TODO: 2023/09/09 [고민] convertImageUrls보다 convertImageIds가 적절하지 않을까 싶어 바꿨는데 어떨까요? 괜찮다면 다른 부분들도 수정하도록 하겠습니다.
    private static List<Long> convertImageIds(final Auction auction) {
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
