package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaQuestionReportRepositoryFixture;
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
class JpaQuestionReportRepositoryTest extends JpaQuestionReportRepositoryFixture {

    @Autowired
    JpaQuestionReportRepository questionReportRepository;

    @Test
    void 질문_신고를_등록한다() {
        // given
        final QuestionReport questionReport = new QuestionReport(신고자, 질문, 신고_내용);

        // when
        final QuestionReport actual = questionReportRepository.save(questionReport);

        // then
        assertThat(actual.getId()).isPositive();
    }
}
