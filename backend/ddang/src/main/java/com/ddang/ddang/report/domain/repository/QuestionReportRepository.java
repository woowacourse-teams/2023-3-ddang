package com.ddang.ddang.report.domain.repository;

import com.ddang.ddang.report.domain.QuestionReport;

import java.util.List;

public interface QuestionReportRepository {

    QuestionReport save(final QuestionReport questionReport);

    boolean existsByQuestionIdAndReporterId(final Long questionId, final Long reporterId);

    List<QuestionReport> findAll();
}
