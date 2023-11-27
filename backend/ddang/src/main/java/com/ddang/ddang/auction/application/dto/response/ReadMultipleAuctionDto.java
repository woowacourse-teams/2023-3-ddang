package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Slice;

public record ReadMultipleAuctionDto(List<AuctionInfoDto> auctionInfoDtos, boolean isLast) {

    public static ReadMultipleAuctionDto of(final Slice<Auction> auctions, final LocalDateTime targetTime) {
        final List<AuctionInfoDto> auctionInfoDtos = auctions.getContent()
                                                             .stream()
                                                             .map(auction -> AuctionInfoDto.of(auction, targetTime))
                                                             .toList();

        return new ReadMultipleAuctionDto(auctionInfoDtos, !auctions.hasNext());
    }

    public record AuctionInfoDto(
            Long id,
            String title,
            String description,
            int bidUnit,
            int startPrice,
            Integer lastBidPrice,
            boolean deleted,
            LocalDateTime registerTime,
            LocalDateTime closingTime,
            List<ReadFullDirectRegionDto> auctionRegions,
            String auctionThumbnailImageStoreName,
            int auctioneerCount,
            String mainCategory,
            String subCategory,
            Long sellerId,
            String sellerProfileStoreName,
            String sellerName,
            Float sellerReliability,
            boolean isSellerDeleted,
            AuctionStatus auctionStatus,
            Long lastBidderId
    ) {

        public static AuctionInfoDto of(final Auction auction, final LocalDateTime targetTime) {
            return new AuctionInfoDto(
                    auction.getId(),
                    auction.getTitle(),
                    auction.getDescription(),
                    auction.getBidUnit().getValue(),
                    auction.getStartPrice().getValue(),
                    auction.findLastBid().map(lastBid -> lastBid.getPrice().getValue()).orElse(null),
                    auction.isDeleted(),
                    auction.getCreatedTime(),
                    auction.getClosingTime(),
                    convertReadRegionsDto(auction),
                    auction.getThumbnailImageStoreName(),
                    auction.getAuctioneerCount(),
                    auction.getSubCategory().getMainCategory().getName(),
                    auction.getSubCategory().getName(),
                    auction.getSeller().getId(),
                    auction.getSeller().getProfileImageStoreName(),
                    auction.getSeller().findName(),
                    auction.getSeller().findReliability(),
                    auction.getSeller().isDeleted(),
                    auction.findAuctionStatus(targetTime),
                    auction.findLastBidder().map(User::getId).orElse(null)
            );
        }

        private static List<ReadFullDirectRegionDto> convertReadRegionsDto(final Auction auction) {
            return auction.getAuctionRegions()
                          .stream()
                          .map(ReadFullDirectRegionDto::from)
                          .toList();
        }
    }
}
