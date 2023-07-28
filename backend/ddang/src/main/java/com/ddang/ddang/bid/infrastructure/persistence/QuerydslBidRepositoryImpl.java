package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ddang.ddang.bid.domain.QBid.bid;

@Repository
@RequiredArgsConstructor
public class QuerydslBidRepositoryImpl implements QuerydslBidRepository {

    private final JPAQueryFactory queryFactory;

    // TODO: 2023/07/28 (고민) orderBy를 id와 createdTime 중 무엇으로 하는 것이 좋은가? (acution은 id임)
    @Override
    public Bid findLastBidByAuctionId(final Long id) {
        return queryFactory
                .selectFrom(bid)
                .where(bid.auction.id.eq(id))
                .orderBy(bid.createdTime.desc())
                .fetchFirst();
    }
}
