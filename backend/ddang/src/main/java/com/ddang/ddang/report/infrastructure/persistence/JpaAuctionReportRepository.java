package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuctionReportRepository extends JpaRepository<AuctionReport, Long>, QuerydslAuctionReportRepository {

    boolean existsByAuctionIdAndReporterId(final Long auctionId, final Long reporterId);
}
