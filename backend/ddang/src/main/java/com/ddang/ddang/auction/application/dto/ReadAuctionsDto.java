package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ReadAuctionsDto(List<ReadAuctionDto> readAuctionDtos, boolean isLast) {

    public static ReadAuctionsDto from(final Slice<Auction> auctions) {
        final List<ReadAuctionDto> readAuctionDtos = auctions.getContent()
                                                             .stream()
                                                             .map(ReadAuctionDto::from)
                                                             .toList();

        return new ReadAuctionsDto(readAuctionDtos, !auctions.hasNext());
    }
}
