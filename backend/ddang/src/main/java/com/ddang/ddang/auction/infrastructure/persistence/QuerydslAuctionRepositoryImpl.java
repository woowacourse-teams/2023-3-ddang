package com.ddang.ddang.auction.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.region.domain.QAuctionRegion.auctionRegion;
import static com.ddang.ddang.region.domain.QRegion.region;

import com.ddang.ddang.auction.domain.Auction;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepositoryImpl implements QuerydslAuctionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Auction> findAuctionsAllByLastAuctionId(final Long lastAuctionId, final int size) {
        return queryFactory
                .selectFrom(auction)
                .where(auction.deleted.isFalse(), lessThanLastAuctionId(lastAuctionId))
                .orderBy(auction.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression lessThanLastAuctionId(final Long lastAuctionId) {
        if (lastAuctionId == null) {
            return null;
        }

        return auction.id.lt(lastAuctionId);
    }

    @Override
    public Optional<Auction> findAuctionWithRegionsById(final Long auctionId) {
        final Auction findAuction = queryFactory
                .selectFrom(auction)
                .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                .leftJoin(region.firstRegion).fetchJoin()
                .leftJoin(region.secondRegion).fetchJoin()
                .where(auction.deleted.isFalse(), auction.id.eq(auctionId))
                .fetchOne();

        return Optional.ofNullable(findAuction);
    }
}
