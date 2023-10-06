package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaAnswerReportRepositoryFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaAnswerReportRepositoryTest extends JpaAnswerReportRepositoryFixture {

    @Autowired
    JpaAnswerReportRepository answerReportRepository;

    @Test
    void 답변을_등록한다() {
        // given
        final AnswerReport answerReport = new AnswerReport(신고자, 답변, 신고_내용);

        // when
        final AnswerReport actual = answerReportRepository.save(answerReport);

        // then
        assertThat(actual.getId()).isPositive();
    }
}
