package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaQuestionReportRepository extends JpaRepository<QuestionReport, Long> {

    boolean existsByQuestionIdAndReporterId(final Long questionId, final Long reporterId);

    @Query("""
        select qr
        from QuestionReport qr
        join fetch qr.reporter
        join fetch qr.question
        order by qr.id asc
    """)
    List<QuestionReport> findAll();
}
