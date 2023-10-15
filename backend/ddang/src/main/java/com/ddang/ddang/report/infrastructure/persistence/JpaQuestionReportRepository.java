package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaQuestionReportRepository extends JpaRepository<QuestionReport, Long> {

    boolean existsByQuestionIdAndReporterId(final Long questionId, final Long reporterId);

    @Query("""
        SELECT qr
        FROM QuestionReport qr
        JOIN FETCH qr.reporter
        JOIN FETCH qr.question
        ORDER BY qr.id ASC
    """)
    List<QuestionReport> findAll();
}
