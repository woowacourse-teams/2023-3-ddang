package com.ddang.ddang.report.domain.repository;

import com.ddang.ddang.report.domain.AuctionReport;

import java.util.List;

public interface AuctionReportRepository {

    AuctionReport save(final AuctionReport auctionReport);

    boolean existsByAuctionIdAndReporterId(final Long auctionId, final Long reporterId);

    List<AuctionReport> findAll();
}
