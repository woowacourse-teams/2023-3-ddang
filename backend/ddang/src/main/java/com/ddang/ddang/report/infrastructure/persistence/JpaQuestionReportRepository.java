package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.QuestionReport;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaQuestionReportRepository extends JpaRepository<QuestionReport, Long> {

    boolean existsByIdAndReporterId(Long id, Long reporterId);

    @EntityGraph(attributePaths = {"reporter", "question"})
    List<QuestionReport> findAllByOrderByIdAsc();
}
