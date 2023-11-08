package com.ddang.ddang.auction.infrastructure.persistence;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static com.ddang.ddang.category.domain.QCategory.category;
import static com.ddang.ddang.region.domain.QAuctionRegion.auctionRegion;
import static com.ddang.ddang.region.domain.QRegion.region;

import com.ddang.ddang.auction.configuration.util.AuctionSortConditionConsts;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.exception.UnsupportedSortConditionException;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.common.helper.QuerydslSliceHelper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionRepository {

    private static final long SLICE_OFFSET = 1L;

    private final JPAQueryFactory queryFactory;

    // 진행 중인 경매 목록 조회
    public Slice<Auction> findProcessAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    ) {
        // 정렬 조건 생성
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        final Sort sort = pageable.getSort();

        // Pageable 내부의 Sort는 다중 지정이 가능하므로 반복문을 통해 처리
        for (final Order order : sort) {
            // 다른 정렬 조건에서도 id 기반의 정렬 조건은 무조건 포함되어야 함
            // 66번째 줄에서 해당 부분을 수행하고 있으므로 이 조건문에서는 과정 생략
            if (AuctionSortConditionConsts.ID.equals(order.getProperty())) {
                continue ;
            }

            // 신뢰도 기반 정렬
            if (AuctionSortConditionConsts.RELIABILITY.equals(order.getProperty())) {
                orderSpecifiers.add(auction.seller.reliability.value.desc());
                continue ;
            }
            // 경매 참여 인원 수 기반 정렬
            if (AuctionSortConditionConsts.AUCTIONEER_COUNT.equals(order.getProperty())) {
                orderSpecifiers.add(auction.auctioneerCount.desc());
                continue ;
            }
            // 마감 임박순 기반 정렬
            // 진행 중인 경매 목록 조회의 경우 마감이 얼마 안남은 상품부터 정렬이 되어야 함
            // 그러므로 closingTime.asc()를 통해 정렬
            if (AuctionSortConditionConsts.CLOSING_TINE.equals(order.getProperty())) {
                orderSpecifiers.add(auction.closingTime.asc());
                continue ;
            }

            // 잘못된 정렬 요청으로 인한 예외 처리
            throw new UnsupportedSortConditionException("지원하지 않는 정렬 방식입니다.");
        }

        // id 기반의 정렬 조건 추가
        orderSpecifiers.add(auction.id.desc());

        // 질의 조건 생성
        final List<BooleanExpression> booleanExpressions = new ArrayList<>();

        // 마감 일자가 현재 시간보다 이후의 시간인 경우 = 경매가 진행중인 경우
        booleanExpressions.add(auction.closingTime.after(LocalDateTime.now()));

        // 필터링 조건이 있는 경우 추가
        if (readAuctionSearchCondition.title() != null) {
            booleanExpressions.add(auction.title.like("%" + readAuctionSearchCondition.title() + "%"));
        }

        // id만 조회
        final List<Long> findAuctionIds = queryFactory.select(auction.id)
                                             .from(auction)
                                             .where(booleanExpressions.toArray(BooleanExpression[]::new))
                                             .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                             .limit(pageable.getPageSize() + SLICE_OFFSET)
                                             .offset(pageable.getOffset())
                                             .fetch();

        // 조회한 id를 기반으로 필요한 모든 Auction 정보 조회
        final List<Auction> findAuctions = queryFactory.selectFrom(auction)
                                                .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                                                .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                                                .leftJoin(region.firstRegion).fetchJoin()
                                                .leftJoin(region.secondRegion).fetchJoin()
                                                .leftJoin(auction.lastBid).fetchJoin()
                                                .join(auction.subCategory, category).fetchJoin()
                                                .join(category.mainCategory).fetchJoin()
                                                .join(auction.seller).fetchJoin()
                                                .where(auction.id.in(findAuctionIds.toArray(Long[]::new)))
                                                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                .fetch();

        // Slice 형태로 변환해 반환
        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }

    // 종료된 경매 목록 조회
    public Slice<Auction> findEndAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    ) {
        // 정렬 조건 생성
        final List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        final Sort sort = pageable.getSort();

        // Pageable 내부의 Sort는 다중 지정이 가능하므로 반복문을 통해 처리
        for (final Order order : sort) {
            // 다른 정렬 조건에서도 id 기반의 정렬 조건은 무조건 포함되어야 함
            // 147번째 줄에서 해당 부분을 수행하고 있으므로 이 조건문에서는 과정 생략
            if (AuctionSortConditionConsts.ID.equals(order.getProperty())) {
                continue ;
            }

            // 신뢰도 기반 정렬
            if (AuctionSortConditionConsts.RELIABILITY.equals(order.getProperty())) {
                orderSpecifiers.add(auction.seller.reliability.value.desc());
                continue ;
            }
            // 경매 참여 인원 수 기반 정렬
            if (AuctionSortConditionConsts.AUCTIONEER_COUNT.equals(order.getProperty())) {
                orderSpecifiers.add(auction.auctioneerCount.desc());
                continue ;
            }
            // 마감 임박순 기반 정렬
            // 종료된 경매 목록 조회의 경우 현재 시간과 가장 가까운 마감 시간 상품부터 정렬되어야 함
            // 그러므로 closingTime.desc()를 정렬
            if (AuctionSortConditionConsts.CLOSING_TINE.equals(order.getProperty())) {
                orderSpecifiers.add(auction.closingTime.desc());
                continue ;
            }

            // 잘못된 정렬 요청으로 인한 예외 처리
            throw new UnsupportedSortConditionException("지원하지 않는 정렬 방식입니다.");
        }

        // id 기반의 정렬 조건 추가
        orderSpecifiers.add(auction.id.desc());

        // 질의 조건 생성
        final List<BooleanExpression> booleanExpressions = new ArrayList<>();

        // 마감 일자가 현재 시간보다 이전의 시간인 경우 = 경매가 종료된 된우
        booleanExpressions.add(auction.closingTime.before(LocalDateTime.now()));

        // 필터링 조건이 있는 경우 추가
        if (readAuctionSearchCondition.title() != null) {
            booleanExpressions.add(auction.title.like("%" + readAuctionSearchCondition.title() + "%"));
        }

        // id만 조회
        final List<Long> findAuctionIds = queryFactory.select(auction.id)
                                                      .from(auction)
                                                      .where(booleanExpressions.toArray(BooleanExpression[]::new))
                                                      .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                      .limit(pageable.getPageSize() + SLICE_OFFSET)
                                                      .offset(pageable.getOffset())
                                                      .fetch();

        // 조회한 id를 기반으로 필요한 모든 Auction 정보 조회
        final List<Auction> findAuctions = queryFactory.selectFrom(auction)
                                                       .leftJoin(auction.auctionRegions, auctionRegion).fetchJoin()
                                                       .leftJoin(auctionRegion.thirdRegion, region).fetchJoin()
                                                       .leftJoin(region.firstRegion).fetchJoin()
                                                       .leftJoin(region.secondRegion).fetchJoin()
                                                       .leftJoin(auction.lastBid).fetchJoin()
                                                       .join(auction.subCategory, category).fetchJoin()
                                                       .join(category.mainCategory).fetchJoin()
                                                       .join(auction.seller).fetchJoin()
                                                       .where(auction.id.in(findAuctionIds.toArray(Long[]::new)))
                                                       .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                                                       .fetch();

        // Slice 형태로 변환해 반환
        return QuerydslSliceHelper.toSlice(findAuctions, pageable);
    }
}
