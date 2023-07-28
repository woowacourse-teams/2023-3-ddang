package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
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
        // TODO 2차 데모데이 이후 리펙터링 예정
        String image,
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
                auction.getImage(),
                auction.getSubCategory().getMainCategory().getName(),
                auction.getSubCategory().getName()
        );
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
