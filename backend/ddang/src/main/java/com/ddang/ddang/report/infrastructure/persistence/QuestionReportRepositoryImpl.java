package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.domain.repository.QuestionReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionReportRepositoryImpl implements QuestionReportRepository {

    private final JpaQuestionReportRepository jpaQuestionReportRepository;

    @Override
    public QuestionReport save(final QuestionReport questionReport) {
        return jpaQuestionReportRepository.save(questionReport);
    }

    @Override
    public boolean existsByQuestionIdAndReporterId(final Long questionId, final Long reporterId) {
        return jpaQuestionReportRepository.existsByQuestionIdAndReporterId(questionId, reporterId);
    }

    @Override
    public List<QuestionReport> findAll() {
        return jpaQuestionReportRepository.findAll();
    }
}
