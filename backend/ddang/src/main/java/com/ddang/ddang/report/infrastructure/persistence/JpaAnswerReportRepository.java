package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaAnswerReportRepository extends JpaRepository<AnswerReport, Long> {

    boolean existsByAnswerIdAndReporterId(final Long answerId, final Long reportId);

    @Query("""
        SELECT ar
        FROM AnswerReport ar
        JOIN FETCH ar.reporter r
        LEFT JOIN FETCH r.profileImage
        JOIN FETCH ar.answer an
        JOIN FETCH an.writer w
        LEFT JOIN FETCH w.profileImage
        ORDER BY ar.id ASC
    """)
    List<AnswerReport> findAll();
}
