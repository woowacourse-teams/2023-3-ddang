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
    public Slice<Auction> findAuctionsAllByLastAuctionId(
            final Long lastAuctionId,
            final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition
    ) {
        final List<OrderSpecifier<?>> orderSpecifiers = calculateOrderSpecifiers(pageable);

        final List<Long> findAuctionIds = queryFactory.select(auction.id)
                                                      .from(auction)
                                                      .where(
                                                              auction.deleted.isFalse(),
                                                              lessThanLastAuctionId(lastAuctionId),
                                                              convertTitleSearchCondition(readAuctionSearchCondition)
                                                      )
                                                      .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                      .limit(pageable.getPageSize() + SLICE_OFFSET)
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

        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }

    private BooleanExpression lessThanLastAuctionId(final Long lastAuctionId) {
        if (lastAuctionId == null) {
            return null;
        }

        return auction.id.lt(lastAuctionId);
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
