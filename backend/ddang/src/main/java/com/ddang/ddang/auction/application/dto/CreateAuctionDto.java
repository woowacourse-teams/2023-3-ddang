package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;

import java.time.LocalDateTime;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startPrice,
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
                request.startPrice(),
                request.closingTime(),
                // TODO 2차 데모데이 이후 리펙터링 예정
                request.image(),
                request.directRegions().first(),
                request.directRegions().second(),
                request.directRegions().third(),
                request.category().main(),
                request.category().sub()
        );
    }

    public Auction toEntity() {
        return Auction.builder()
                      .title(title)
                      .description(description)
                      .bidUnit(new BidUnit(bidUnit))
                      .startPrice(new Price(startPrice))
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
