package com.ddang.ddang.auction.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.category.domain.QCategory.category;
import static com.ddang.ddang.region.domain.QAuctionRegion.auctionRegion;
import static com.ddang.ddang.region.domain.QRegion.region;

import com.ddang.ddang.auction.configuration.util.AuctionSortConditionConsts;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.util.AuctionSortCondition;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.common.helper.QuerydslSliceHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepositoryImpl implements QuerydslAuctionRepository {

    private static final long SLICE_OFFSET = 1L;

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Auction> findAuctionsAllByCondition(
            final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition
    ) {
        final List<OrderSpecifier<?>> orderSpecifiers = calculateOrderSpecifiers(pageable);
        final List<BooleanExpression> booleanExpressions = calculateBooleanExpressions(readAuctionSearchCondition);
        final List<Long> findAuctionIds = findAuctionIds(booleanExpressions, orderSpecifiers, pageable);
        final List<Auction> findAuctions = findAuctionsByIdsAndOrderSpecifiers(findAuctionIds, orderSpecifiers);

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
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

    private List<OrderSpecifier<?>> calculateOrderSpecifiers(final Pageable pageable) {
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>(convertOrderSpecifiers(pageable));

        orderSpecifiers.add(auction.id.desc());

        return orderSpecifiers;
    }

    private List<OrderSpecifier<?>> convertOrderSpecifiers(final Pageable pageable) {
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        final Sort sort = pageable.getSort();

        for (final Order order : sort) {
            if (AuctionSortConditionConsts.ID.equals(order.getProperty())) {
                return Collections.emptyList();
            }

            orderSpecifiers.add(AuctionSortCondition.convert(order));
        }

        return orderSpecifiers;
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
                           .leftJoin(auction.subCategory, category).fetchJoin()
                           .leftJoin(category.mainCategory).fetchJoin()
                           .leftJoin(auction.seller).fetchJoin()
                           .leftJoin(auction.lastBid).fetchJoin()
                           .where(auction.id.in(targetIds.toArray(Long[]::new)))
                           .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                           .fetch();
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

    @Override
    public Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable) {
        final List<BooleanExpression> booleanExpressions = List.of(auction.seller.id.eq(userId));
        final List<OrderSpecifier<?>> orderSpecifiers = List.of(auction.id.desc());
        final List<Long> findAuctionIds = findAuctionIds(booleanExpressions, orderSpecifiers, pageable);
        final List<Auction> findAuctions = findAuctionsByIdsAndOrderSpecifiers(
                findAuctionIds,
                List.of(auction.id.desc())
        );

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }
}
