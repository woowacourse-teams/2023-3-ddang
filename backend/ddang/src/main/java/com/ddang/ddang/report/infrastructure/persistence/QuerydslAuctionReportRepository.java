package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;

import java.util.List;

public interface QuerydslAuctionReportRepository {

    List<AuctionReport> findAll();
}
