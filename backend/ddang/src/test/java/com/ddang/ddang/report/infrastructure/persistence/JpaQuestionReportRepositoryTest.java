package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaQuestionReportRepositoryFixture;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

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

    @Test
    void 신고한_질문이라면_참을_반환한다() {
        // when
        final boolean actual = questionReportRepository.existsByIdAndReporterId(이미_신고한_질문.getId(), 신고자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 신고전인_질문이라면_거짓을_반환한다() {
        // when
        final boolean actual = questionReportRepository.existsByIdAndReporterId(질문.getId(), 신고자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 신고된_질문_목록을_조회한다() {
        // when
        final List<QuestionReport> actual = questionReportRepository.findAllByOrderByIdAsc();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(4);
            softAssertions.assertThat(actual.get(0)).isEqualTo(질문_신고1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(질문_신고2);
            softAssertions.assertThat(actual.get(2)).isEqualTo(질문_신고3);
            softAssertions.assertThat(actual.get(3)).isEqualTo(질문_신고4);
        });
    }
}
