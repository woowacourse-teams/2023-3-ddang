package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ddang.ddang.report.domain.QAuctionReport.auctionReport;

@Repository
@RequiredArgsConstructor
public class QuerydslAuctionReportRepositoryImpl implements QuerydslAuctionReportRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuctionReport> findAll() {
        return queryFactory.selectFrom(auctionReport)
                           .leftJoin(auctionReport.reporter).fetchJoin()
                           .leftJoin(auctionReport.auction).fetchJoin()
                           .fetch();
    }
}
