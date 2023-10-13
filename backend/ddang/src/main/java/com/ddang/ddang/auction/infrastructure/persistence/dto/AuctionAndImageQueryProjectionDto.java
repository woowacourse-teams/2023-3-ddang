package com.ddang.ddang.auction.infrastructure.persistence.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.image.domain.AuctionImage;
import com.querydsl.core.annotations.QueryProjection;

public record AuctionAndImageQueryProjectionDto(Auction auction, AuctionImage auctionImage) {

    @QueryProjection
    public AuctionAndImageQueryProjectionDto {
    }

    public AuctionAndImageDto toDto() {
        return new AuctionAndImageDto(this.auction, this.auctionImage);
    }
}
