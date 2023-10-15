package com.ddang.ddang.report.domain.repository;

import com.ddang.ddang.report.domain.AnswerReport;

import java.util.List;

public interface AnswerReportRepository {

    AnswerReport save(final AnswerReport answerReport);

    boolean existsByAnswerIdAndReporterId(final Long answerId, final Long reportId);

    List<AnswerReport> findAll();
}
