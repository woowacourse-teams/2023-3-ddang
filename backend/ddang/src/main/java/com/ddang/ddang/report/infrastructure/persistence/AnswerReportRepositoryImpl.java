package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.domain.repository.AnswerReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnswerReportRepositoryImpl implements AnswerReportRepository {

    private final JpaAnswerReportRepository jpaAnswerReportRepository;

    @Override
    public AnswerReport save(final AnswerReport answerReport) {
        return jpaAnswerReportRepository.save(answerReport);
    }

    @Override
    public boolean existsByAnswerIdAndReporterId(final Long answerId, final Long reportId) {
        return jpaAnswerReportRepository.existsByAnswerIdAndReporterId(answerId, reportId);
    }

    @Override
    public List<AnswerReport> findAll() {
        return jpaAnswerReportRepository.findAll();
    }
}
