package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.presentation.dto.CreateAuctionRequest;

import java.time.LocalDateTime;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startBidPrice,
        LocalDateTime closingTime,

        // TODO 2차 데모데이 이후 리펙터링 예정
        String image,
        String firstRegion,
        String secondRegion,
        String thirdRegion,
        String mainCategory,
        String subCategory
) {

    public static CreateAuctionDto from(final CreateAuctionRequest request) {
        return new CreateAuctionDto(
                request.title(),
                request.description(),
                request.bidUnit(),
                request.startBidPrice(),
                request.closingTime(),
                // TODO 2차 데모데이 이후 리펙터링 예정
                request.image(),
                request.firstRegion(),
                request.secondRegion(),
                request.thirdRegion(),
                request.mainCategory(),
                request.subCategory()
        );
    }

    public Auction toEntity() {
        return Auction.builder()
                      .title(title)
                      .description(description)
                      .bidUnit(new BidUnit(bidUnit))
                      .startBidPrice(new Price(startBidPrice))
                      .closingTime(closingTime)
                      // TODO 2차 데모데이 이후 리펙터링 예정
                      .image(image)
                      .firstRegion(firstRegion)
                      .secondRegion(secondRegion)
                      .thirdRegion(thirdRegion)
                      .mainCategory(mainCategory)
                      .subCategory(subCategory)
                      .build();
    }
}
