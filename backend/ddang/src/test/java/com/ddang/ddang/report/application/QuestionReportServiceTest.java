package com.ddang.ddang.report.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.report.application.exception.InvalidQuestionReportException;
import com.ddang.ddang.report.application.fixture.QuestionReportServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuestionReportServiceTest extends QuestionReportServiceFixture {

    @Autowired
    QuestionReportService questionReportService;

    @Test
    void 질문을_신고한다() {
        // when
        final Long actual = questionReportService.create(질문_신고_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_질문_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionReportService.create(존재하지_않는_질문_신고_요청_dto))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessage("해당 질문을 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_사용자가_질문_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionReportService.create(존재하지_않는_사용자가_질문_신고_요청_dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 질문자가_본_질문_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionReportService.create(질문자가_본인_질문_신고_요청_dto))
                .isInstanceOf(InvalidQuestionReportException.class)
                .hasMessage("본인 질문입니다.");
    }

    @Test
    void 이미_신고한_질문_신고시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionReportService.create(이미_신고한_질문_신고_요청_dto))
                .isInstanceOf(InvalidQuestionReportException.class)
                .hasMessage("이미 신고한 질문입니다.");
    }
}
