package com.ddang.ddang.auction.domain.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.image.domain.AuctionImage;

public record AuctionAndImageDto(Auction auction, AuctionImage auctionImage) {
}
