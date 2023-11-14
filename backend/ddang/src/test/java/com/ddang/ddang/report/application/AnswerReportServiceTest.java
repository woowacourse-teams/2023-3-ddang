package com.ddang.ddang.report.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.qna.application.exception.AnswerNotFoundException;
import com.ddang.ddang.report.application.dto.ReadAnswerReportDto;
import com.ddang.ddang.report.application.exception.InvalidAnswererReportException;
import com.ddang.ddang.report.application.fixture.AnswerReportServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AnswerReportServiceTest extends AnswerReportServiceFixture {

    @Autowired
    AnswerReportService answerReportService;

    @Test
    void 답변_신고를_등록한다() {
        // when
        final Long actual = answerReportService.create(답변_신고_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_답변_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> answerReportService.create(존재하지_않는_답변_신고_요청_dto))
                .isInstanceOf(AnswerNotFoundException.class);
    }

    @Test
    void 존재하지_않는_사용자가_답변_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> answerReportService.create(존재하지_않는_사용자가_답변_신고_요청_dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 답변자가_본인_답변_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> answerReportService.create(답변자가_본인_답변_신고_요청_dto))
                .isInstanceOf(InvalidAnswererReportException.class)
                .hasMessage("본인 답변입니다.");
    }

    @Test
    void 이미_신고한_답변_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> answerReportService.create(이미_신고한_답변_신고_요청_dto))
                .isInstanceOf(InvalidAnswererReportException.class)
                .hasMessage("이미 신고한 답변입니다.");
    }

    @Test
    void 전체_신고_목록을_조회한다() {
        // when
        final List<ReadAnswerReportDto> actual = answerReportService.readAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).reporterDto().id()).isEqualTo(이미_신고한_신고자1.getId());
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(답변_신고1.getId());
            softAssertions.assertThat(actual.get(1).reporterDto().id()).isEqualTo(이미_신고한_신고자2.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(답변_신고2.getId());
            softAssertions.assertThat(actual.get(2).reporterDto().id()).isEqualTo(이미_신고한_신고자3.getId());
            softAssertions.assertThat(actual.get(2).id()).isEqualTo(답변_신고3.getId());
        });
    }
}
