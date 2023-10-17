package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.configuration.util.AuctionSortConditionConsts;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.exception.UnsupportedSortConditionException;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.common.helper.QuerydslSliceHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.bid.domain.QBid.bid;
import static com.ddang.ddang.category.domain.QCategory.category;
import static com.ddang.ddang.region.domain.QAuctionRegion.auctionRegion;
import static com.ddang.ddang.region.domain.QRegion.region;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepository {

    private static final long SLICE_OFFSET = 1L;
    private static final int HIGH_PRIORITY = 2;
    private static final int LOW_PRIORITY = 1;

    private final JPAQueryFactory queryFactory;

    public Slice<Auction> findAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    ) {
        final List<OrderSpecifier<?>> orderSpecifiers = calculateOrderSpecifiers(pageable);
        final List<BooleanExpression> booleanExpressions = calculateBooleanExpressions(readAuctionSearchCondition);
        final List<Long> findAuctionIds = findAuctionIds(booleanExpressions, orderSpecifiers, pageable);
        final List<Auction> findAuctions = findAuctionsByIdsAndOrderSpecifiers(findAuctionIds, orderSpecifiers);

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }

    private List<OrderSpecifier<?>> calculateOrderSpecifiers(final Pageable pageable) {
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        orderSpecifiers.add(closingTimeOrderSpecifier());
        orderSpecifiers.addAll(processOrderSpecifiers(pageable));
        orderSpecifiers.add(auction.id.desc());

        return orderSpecifiers;
    }

    private List<OrderSpecifier<?>> processOrderSpecifiers(final Pageable pageable) {
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        final Sort sort = pageable.getSort();

        for (final Order order : sort) {
            if (AuctionSortConditionConsts.ID.equals(order.getProperty())) {
                return Collections.emptyList();
            }

            orderSpecifiers.add(processOrderSpecifierByCondition(order));
        }

        return orderSpecifiers;
    }

    private OrderSpecifier<?> processOrderSpecifierByCondition(final Order order) {
        if (AuctionSortConditionConsts.RELIABILITY.equals(order.getProperty())) {
            return auction.seller.reliability.value.desc();
        }
        if (AuctionSortConditionConsts.AUCTIONEER_COUNT.equals(order.getProperty())) {
            return auction.auctioneerCount.desc();
        }
        if (AuctionSortConditionConsts.CLOSING_TINE.equals(order.getProperty())) {
            return auction.closingTime.asc();
        }

        throw new UnsupportedSortConditionException("지원하지 않는 정렬 방식입니다.");
    }

    private OrderSpecifier<Integer> closingTimeOrderSpecifier() {
        final LocalDateTime now = LocalDateTime.now();

        return new CaseBuilder()
                .when(auction.closingTime.after(now)).then(LOW_PRIORITY)
                .otherwise(HIGH_PRIORITY)
                .asc();
    }

    private List<BooleanExpression> calculateBooleanExpressions(final ReadAuctionSearchCondition searchCondition) {
        final List<BooleanExpression> booleanExpressions = new ArrayList<>();

        booleanExpressions.add(auction.deleted.isFalse());

        final BooleanExpression titleBooleanExpression = convertTitleSearchCondition(searchCondition);

        if (titleBooleanExpression != null) {
            booleanExpressions.add(titleBooleanExpression);
        }

        return booleanExpressions;
    }

    private List<Long> findAuctionIds(
            final List<BooleanExpression> booleanExpressions,
            final List<OrderSpecifier<?>> orderSpecifiers,
            final Pageable pageable
    ) {
        return queryFactory.select(auction.id)
                           .from(auction)
                           .where(booleanExpressions.toArray(BooleanExpression[]::new))
                           .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                           .limit(pageable.getPageSize() + SLICE_OFFSET)
                           .offset(pageable.getOffset())
                           .fetch();
    }

    private BooleanExpression convertTitleSearchCondition(final ReadAuctionSearchCondition readAuctionSearchCondition) {
        final String titleSearchCondition = readAuctionSearchCondition.title();

        if (titleSearchCondition == null) {
            return null;
        }

        return auction.title.like("%" + titleSearchCondition + "%");
    }

    private List<Auction> findAuctionsByIdsAndOrderSpecifiers(
            final List<Long> targetIds,
            final List<OrderSpecifier<?>> orderSpecifiers
    ) {
        return queryFactory.selectFrom(auction)
                           .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                           .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                           .leftJoin(region.firstRegion).fetchJoin()
                           .leftJoin(region.secondRegion).fetchJoin()
                           .leftJoin(auction.lastBid).fetchJoin()
                           .join(auction.subCategory, category).fetchJoin()
                           .join(category.mainCategory).fetchJoin()
                           .join(auction.seller).fetchJoin()
                           .where(auction.id.in(targetIds.toArray(Long[]::new)))
                           .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                           .fetch();
    }

    public Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable) {
        final List<BooleanExpression> booleanExpressions = List.of(
                auction.seller.id.eq(userId),
                auction.deleted.isFalse()
        );
        final List<OrderSpecifier<?>> orderSpecifiers = List.of(auction.id.desc());
        final List<Long> findAuctionIds = findAuctionIds(booleanExpressions, orderSpecifiers, pageable);
        final List<Auction> findAuctions = findAuctionsByIdsAndOrderSpecifiers(
                findAuctionIds,
                List.of(auction.id.desc())
        );

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }

    public Slice<Auction> findAuctionsAllByBidderId(final Long bidderId, final Pageable pageable) {
        final List<Long> findAuctionIds = queryFactory.select(bid.auction.id)
                                                      .from(bid)
                                                      .where(bid.bidder.id.eq(bidderId))
                                                      .groupBy(bid.auction.id)
                                                      .orderBy(bid.id.max().desc())
                                                      .limit(pageable.getPageSize() + SLICE_OFFSET)
                                                      .offset(pageable.getOffset())
                                                      .fetch();
        final List<Auction> findAuctions = findAuctionsByIdsAndOrderSpecifiers(findAuctionIds, Collections.emptyList());

        findAuctions.sort((firstAuction, secondAuction) -> {
            int firstAuctionIndex = findAuctionIds.indexOf(firstAuction.getId());
            int secondAuctionIndex = findAuctionIds.indexOf(secondAuction.getId());

            return Integer.compare(firstAuctionIndex, secondAuctionIndex);
        });

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }

    public boolean existsBySellerIdAndAuctionIsOngoing(final Long userId, final LocalDateTime now) {
        return queryFactory.selectFrom(auction)
                           .join(auction.seller).fetchJoin()
                           .where(auction.seller.id.eq(userId)
                                                   .and(auction.deleted.isFalse())
                                                   .and(auction.closingTime.after(now))
                           )
                           .fetchFirst() != null;
    }

    public boolean existsLastBidByUserIdAndAuctionIsOngoing(final Long userId, final LocalDateTime now) {
        return queryFactory.selectFrom(auction)
                           .join(auction.lastBid, bid).fetchJoin()
                           .join(bid.bidder).fetchJoin()
                           .where(bid.bidder.id.eq(userId)
                                               .and(auction.deleted.isFalse())
                                               .and(auction.closingTime.after(now))
                           )
                           .fetchFirst() != null;
    }
}
