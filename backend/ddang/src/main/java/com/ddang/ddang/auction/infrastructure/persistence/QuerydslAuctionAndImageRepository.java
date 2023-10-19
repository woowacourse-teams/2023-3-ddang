package com.ddang.ddang.auction.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.bid.domain.QBid.bid;
import static com.ddang.ddang.image.domain.QAuctionImage.auctionImage;

import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;
import com.ddang.ddang.auction.domain.repository.AuctionAndImageRepository;
import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageQueryProjectionDto;
import com.ddang.ddang.auction.infrastructure.persistence.dto.QAuctionAndImageQueryProjectionDto;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionAndImageRepository implements AuctionAndImageRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AuctionAndImageDto> findDtoByAuctionId(final Long auctionId) {
        final AuctionAndImageQueryProjectionDto auctionAndImageQueryProjectionDto =
                queryFactory.select(new QAuctionAndImageQueryProjectionDto(auction, auctionImage))
                            .from(auction)
                            .leftJoin(auction.lastBid, bid).fetchJoin()
                            .leftJoin(bid.bidder).fetchJoin()
                            .leftJoin(auctionImage).on(auctionImage.id.eq(
                                    JPAExpressions
                                            .select(auctionImage.id.min())
                                            .from(auctionImage)
                                            .where(auctionImage.auction.id.eq(auction.id))
                                            .groupBy(auctionImage.auction.id)
                            )).fetchJoin()
                            .where(auction.id.eq(auctionId))
                            .fetchOne();

        if (auctionAndImageQueryProjectionDto == null) {
            return Optional.empty();
        }

        return Optional.of(auctionAndImageQueryProjectionDto.toDto());
    }
}
