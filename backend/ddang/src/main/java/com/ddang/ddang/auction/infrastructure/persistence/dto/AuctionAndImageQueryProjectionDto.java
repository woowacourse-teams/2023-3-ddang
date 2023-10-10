package com.ddang.ddang.auction.infrastructure.persistence.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.image.domain.AuctionImage;
import com.querydsl.core.annotations.QueryProjection;

public record AuctionAndImageQueryProjectionDto(Auction auction, AuctionImage auctionImage) {

    @QueryProjection
    public AuctionAndImageQueryProjectionDto {
    }

    // TODO: 2023/09/22 dto이름 정해지면 명확한 dto이름으로 바꾸기 
    public AuctionAndImageDto toDto() {
        return new AuctionAndImageDto(this.auction, this.auctionImage);
    }
}
