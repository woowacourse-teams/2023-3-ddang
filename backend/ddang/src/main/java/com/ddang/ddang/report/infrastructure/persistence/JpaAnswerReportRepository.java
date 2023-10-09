package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAnswerReportRepository extends JpaRepository<AnswerReport, Long> {

    boolean existsByIdAndReporterId(final Long id, final Long ReporterId);

    @EntityGraph(attributePaths = {"reporter", "answer", "answer.question", "answer.question.auction", "answer.question.auction.seller"})
    List<AnswerReport> findAllByOrderByIdAsc();
}
