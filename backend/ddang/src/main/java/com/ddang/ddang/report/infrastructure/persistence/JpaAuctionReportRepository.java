package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AuctionReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAuctionReportRepository extends JpaRepository<AuctionReport, Long> {

    boolean existsByAuctionIdAndReporterId(final Long auctionId, final Long reporterId);

    @Query("""
        select ar
        from AuctionReport ar
        join fetch ar.reporter
        join fetch ar.auction a
        join fetch a.seller
        order by ar.id asc
    """)
    List<AuctionReport> findAll();
}
