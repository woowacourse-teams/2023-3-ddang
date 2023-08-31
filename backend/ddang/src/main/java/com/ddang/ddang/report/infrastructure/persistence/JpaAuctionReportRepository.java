package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAuctionReportRepository extends JpaRepository<AuctionReport, Long> {

    boolean existsByAuctionIdAndReporterId(final Long auctionId, final Long reporterId);

    @Override
    @EntityGraph(attributePaths = {"reporter", "auction", "auction.seller"})
    List<AuctionReport> findAll();
}
