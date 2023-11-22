package com.ddang.ddang.auction.application.dto.request;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startPrice,
        LocalDateTime closingTime,
        List<Long> thirdRegionIds,
        Long subCategoryId,
        List<MultipartFile> auctionImages,
        Long sellerId
) {

    public static CreateAuctionDto of(
            final CreateAuctionRequest request,
            final List<MultipartFile> images,
            final Long sellerId
    ) {
        return new CreateAuctionDto(
                request.title(),
                request.description(),
                request.bidUnit(),
                request.startPrice(),
                request.closingTime(),
                calculateThirdRegionIds(request),
                request.subCategoryId(),
                images,
                sellerId
        );
    }

    private static List<Long> calculateThirdRegionIds(final CreateAuctionRequest request) {
        if (request.thirdRegionIds() == null) {
            return Collections.emptyList();
        }

        return request.thirdRegionIds();
    }

    public Auction toEntity(final User seller, final Category subCategory) {
        return Auction.builder()
                      .title(title)
                      .description(description)
                      .bidUnit(new BidUnit(bidUnit))
                      .startPrice(new Price(startPrice))
                      .closingTime(closingTime)
                      .seller(seller)
                      .subCategory(subCategory)
                      .build();
    }
}
