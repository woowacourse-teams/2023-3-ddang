package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ReadAuctionsDto(List<ReadAuctionDto> readAuctionDtos, boolean isLast) {

    public static ReadAuctionsDto of(final Slice<Auction> auctions, final LocalDateTime targetTime) {
        final List<ReadAuctionDto> readAuctionDtos = auctions.getContent()
                                                             .stream()
                                                             .map(auction -> ReadAuctionDto.of(auction, targetTime))
                                                             .toList();

        return new ReadAuctionsDto(readAuctionDtos, !auctions.hasNext());
    }
}
