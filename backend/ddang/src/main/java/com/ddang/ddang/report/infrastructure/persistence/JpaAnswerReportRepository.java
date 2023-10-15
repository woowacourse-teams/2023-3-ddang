package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAnswerReportRepository extends JpaRepository<AnswerReport, Long> {

    boolean existsByAnswerIdAndReporterId(final Long answerId, final Long ReporterId);

    @Query("""
        select ar
        from AnswerReport ar
        join fetch ar.reporter
        join fetch ar.answer an
        join fetch an.question q
        join fetch q.auction a
        join fetch a.seller
        order by ar.id asc
    """)
    List<AnswerReport> findAll();
}
