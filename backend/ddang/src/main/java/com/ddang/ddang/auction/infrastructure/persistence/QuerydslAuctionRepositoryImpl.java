package com.ddang.ddang.auction.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.category.domain.QCategory.category;
import static com.ddang.ddang.region.domain.QAuctionRegion.auctionRegion;
import static com.ddang.ddang.region.domain.QRegion.region;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import com.ddang.ddang.common.helper.QuerydslSliceHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepositoryImpl implements QuerydslAuctionRepository {

    private static final long SLICE_OFFSET = 1L;

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Auction> findAuctionsAllByLastAuctionId(final ReadAuctionCondition readAuctionCondition) {
        final List<OrderSpecifier<?>> orderSpecifiers = calculateOrderSpecifiers(readAuctionCondition);

        final List<Long> findAuctionIds = queryFactory.select(auction.id)
                                                      .from(auction)
                                                      .where(
                                                              auction.deleted.isFalse(),
                                                              lessThanLastTarget(readAuctionCondition),
                                                              convertTitleSearchCondition(readAuctionCondition)
                                                      )
                                                      .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                      .limit(readAuctionCondition.size() + SLICE_OFFSET)
                                                      .fetch();

        final List<Auction> findAuctions = queryFactory.selectFrom(auction)
                                                       .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                                                       .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                                                       .leftJoin(region.firstRegion).fetchJoin()
                                                       .leftJoin(region.secondRegion).fetchJoin()
                                                       .leftJoin(auction.subCategory, category).fetchJoin()
                                                       .leftJoin(category.mainCategory).fetchJoin()
                                                       .leftJoin(auction.seller).fetchJoin()
                                                       .leftJoin(auction.lastBid).fetchJoin()
                                                       .where(auction.id.in(findAuctionIds.toArray(Long[]::new)))
                                                       .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                       .fetch();

        return QuerydslSliceHelper.toSlice(findAuctions, readAuctionCondition);
    }

    private BooleanExpression lessThanLastTarget(final ReadAuctionCondition readAuctionCondition) {
        if (readAuctionCondition.isFirstPageRequest()) {
            return null;
        }
        if (readAuctionCondition.isSortByAuctioneerCount()) {
            return auction.auctioneerCount.lt(readAuctionCondition.lastAuctioneerCount());
        }
        if (readAuctionCondition.isSortByClosingTime()) {
            return auction.closingTime.gt(readAuctionCondition.lastClosingTime());
        }
        if (readAuctionCondition.isSortByReliability()) {
            return auction.seller.reliability.lt(readAuctionCondition.lastReliability());
        }

        return auction.id.lt(readAuctionCondition.lastAuctionId());
    }

    private BooleanExpression convertTitleSearchCondition(final ReadAuctionCondition readAuctionCondition) {
        final String titleSearchCondition = readAuctionCondition.title();

        if (titleSearchCondition == null) {
            return null;
        }

        return auction.title.like("%" + titleSearchCondition + "%");
    }

    private List<OrderSpecifier<?>> calculateOrderSpecifiers(final ReadAuctionCondition readAuctionCondition) {
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        final OrderSpecifier<?> orderSpecifier = convertOrderSpecifiers(readAuctionCondition);

        orderSpecifiers.add(auction.id.desc());

        if (orderSpecifier == null) {
            return orderSpecifiers;
        }

        orderSpecifiers.add(0, orderSpecifier);
        return orderSpecifiers;
    }

    private OrderSpecifier<?> convertOrderSpecifiers(final ReadAuctionCondition readAuctionCondition) {
        if (readAuctionCondition.isSortByReliability()) {
            return auction.seller.reliability.desc();
        }
        if (readAuctionCondition.isSortByAuctioneerCount()) {
            return auction.auctioneerCount.desc();
        }
        if (readAuctionCondition.isSortByClosingTime()) {
            return auction.closingTime.asc();
        }

        return null;
    }

    @Override
    public Optional<Auction> findAuctionById(final Long auctionId) {
        final Auction findAuction = queryFactory.selectFrom(auction)
                                                .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                                                .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                                                .leftJoin(region.firstRegion).fetchJoin()
                                                .leftJoin(region.secondRegion).fetchJoin()
                                                .leftJoin(auction.subCategory, category).fetchJoin()
                                                .leftJoin(category.mainCategory).fetchJoin()
                                                .leftJoin(auction.seller).fetchJoin()
                                                .leftJoin(auction.lastBid).fetchJoin()
                                                .where(auction.deleted.isFalse(), auction.id.eq(auctionId))
                                                .fetchOne();

        return Optional.ofNullable(findAuction);
    }
}
