package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ddang.ddang.auction.domain.QAuction.auction;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepositoryImpl implements QuerydslAuctionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Auction> findAuctionsAllByLastAuctionId(final Long lastAuctionId, final int size) {
        return queryFactory
                .selectFrom(auction)
                .where(auction.deleted.isFalse().and(lessThanLastAuctionId(lastAuctionId)))
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
}
