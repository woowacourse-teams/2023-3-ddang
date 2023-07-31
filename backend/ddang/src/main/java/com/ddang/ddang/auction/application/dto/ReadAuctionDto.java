package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
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
        Integer winningBidPrice,
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
                convertPrice(auction.getLastBidPrice()),
                convertPrice(auction.getWinningBidPrice()),
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

    private static Integer convertPrice(final Price price) {
        if (price == null) {
            return null;
        }

        return price.getValue();
    }

    private static List<ReadRegionsDto> convertReadRegionsDto(final Auction auction) {
        return auction.getAuctionRegions()
                      .stream()
                      .map(ReadRegionsDto::from)
                      .toList();
    }
}
