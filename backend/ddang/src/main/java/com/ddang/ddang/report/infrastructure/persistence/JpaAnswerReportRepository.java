package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAnswerReportRepository extends JpaRepository<AnswerReport, Long> {

    boolean existsByIdAndReporterId(Long id, Long ReporterId);

    @EntityGraph(attributePaths = {"reporter", "answer"})
    List<AnswerReport> findAllByOrderByIdAsc();
}
