package com.ddang.ddang.report.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.report.application.fixture.AnswerReportServiceFixture;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.infrastructure.persistence.JpaAnswerReportRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AnswerReportServiceTest extends AnswerReportServiceFixture {

    @Autowired
    JpaAnswerReportRepository answerReportRepository;

    @Test
    void 답변_신고를_등록한다() {
        // given
        final AnswerReport answerReport = new AnswerReport(신고자, 답변, 신고_내용);

        // when
        final AnswerReport actual = answerReportRepository.save(answerReport);

        // then
        assertThat(actual.getId()).isPositive();
    }
}
