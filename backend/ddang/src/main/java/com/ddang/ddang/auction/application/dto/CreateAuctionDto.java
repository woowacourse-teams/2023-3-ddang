package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.category.domain.Category;
import java.time.LocalDateTime;
import java.util.List;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startPrice,
        LocalDateTime closingTime,

        List<CreateRegionDto> createRegionDtos,

        // TODO 2차 데모데이 이후 리펙터링 예정
        String image,
        Long subCategoryId
) {

    public static CreateAuctionDto from(final CreateAuctionRequest request) {
        return new CreateAuctionDto(
                request.title(),
                request.description(),
                request.bidUnit(),
                request.startPrice(),
                request.closingTime(),
                convertCreateRegionDto(request),
                // TODO 2차 데모데이 이후 리펙터링 예정
                request.images().get(0),
                request.subCategoryId()
        );
    }

    private static List<CreateRegionDto> convertCreateRegionDto(final CreateAuctionRequest request) {
        return request.directRegions()
                      .stream()
                      .map(CreateRegionDto::from)
                      .toList();
    }

    public Auction toEntity(final Category subCategory) {
        return Auction.builder()
                      .title(title)
                      .description(description)
                      .bidUnit(new BidUnit(bidUnit))
                      .startPrice(new Price(startPrice))
                      .closingTime(closingTime)
                      .subCategory(subCategory)
                      // TODO 2차 데모데이 이후 리펙터링 예정
                      .image(image)
                      .build();
    }
}
