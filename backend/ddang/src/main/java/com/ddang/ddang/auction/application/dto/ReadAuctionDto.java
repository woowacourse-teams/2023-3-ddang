package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;

import java.time.LocalDateTime;

public record ReadAuctionDto(
        Long id,
        String title,
        String description,
        int bidUnit,
        int startBidPrice,
        Integer lastBidPrice,
        Integer winningBidPrice,
        boolean deleted,
        LocalDateTime registerTime,
        LocalDateTime closingTime,
        // TODO 2차 데모데이 이후 리펙터링 예정
        String image,
        String firstRegion,
        String secondRegion,
        String thirdRegion,
        String mainCategory,
        String subCategory
) {

    public static ReadAuctionDto from(final Auction auction) {
        return new ReadAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getBidUnit().getValue(),
                auction.getStartBidPrice().getValue(),
                convertPrice(auction.getLastBidPrice()),
                convertPrice(auction.getWinningBidPrice()),
                auction.isDeleted(),
                auction.getCreatedTime(),
                auction.getClosingTime(),
                // TODO 2차 데모데이 이후 리펙터링 예정
                auction.getImage(),
                auction.getFirstRegion(),
                auction.getSecondRegion(),
                auction.getThirdRegion(),
                auction.getMainCategory(),
                auction.getSubCategory()
        );
    }

    private static Integer convertPrice(final Price price) {
        if (price == null) {
            return null;
        }

        return price.getValue();
    }
}
