package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.report.domain.repository.AuctionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuctionReportRepositoryImpl implements AuctionReportRepository {

    private final JpaAuctionReportRepository jpaAuctionReportRepository;

    @Override
    public AuctionReport save(final AuctionReport auctionReport) {
        return jpaAuctionReportRepository.save(auctionReport);
    }

    @Override
    public boolean existsByAuctionIdAndReporterId(final Long auctionId, final Long reporterId) {
        return jpaAuctionReportRepository.existsByAuctionIdAndReporterId(auctionId, reporterId);
    }

    @Override
    public List<AuctionReport> findAll() {
        return jpaAuctionReportRepository.findAll();
    }
}
