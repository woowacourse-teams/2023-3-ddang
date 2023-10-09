package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.infrastructure.persistence.fixture.JpaAnswerReportRepositoryFixture;
import org.assertj.core.api.SoftAssertions;
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

    @Test
    void 신고한_답변이라면_참을_반환한다() {
        // when
        final boolean actual = answerReportRepository.existsByAnswerIdAndReporterId(이미_신고된_답변.getId(), 신고자.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 신고_전인_답변이라면_거짓을_반환한다() {
        // when
        final boolean actual = answerReportRepository.existsByAnswerIdAndReporterId(답변.getId(), 신고자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 신고된_답변_목록을_조회한다() {
        // when
        final List<AnswerReport> actual = answerReportRepository.findAllByOrderByIdAsc();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(4);
            softAssertions.assertThat(actual.get(0)).isEqualTo(답변_신고1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(답변_신고2);
            softAssertions.assertThat(actual.get(2)).isEqualTo(답변_신고3);
            softAssertions.assertThat(actual.get(3)).isEqualTo(답변_신고4);
        });
    }
}
