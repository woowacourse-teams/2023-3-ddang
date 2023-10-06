package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerReportRepository extends JpaRepository<AnswerReport, Long> {

    boolean existsByIdAndReporterId(Long id, Long ReporterId);
}
