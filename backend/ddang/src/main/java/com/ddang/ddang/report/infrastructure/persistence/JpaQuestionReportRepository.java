package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.QuestionReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionReportRepository extends JpaRepository<QuestionReport, Long> {

    boolean existsByIdAndReporterId(Long id, Long reporterId);
}
