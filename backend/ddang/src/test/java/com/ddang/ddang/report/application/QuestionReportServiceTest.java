package com.ddang.ddang.report.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.report.application.dto.ReadQuestionReportDto;
import com.ddang.ddang.report.application.exception.InvalidQuestionReportException;
import com.ddang.ddang.report.application.fixture.QuestionReportServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    void 질문자가_본인_질문_신고시_예외가_발생한다() {
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

    @Test
    void 전체_신고_목록을_조회한다() {
        // when
        final List<ReadQuestionReportDto> actual = questionReportService.readAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0).reporterDto().id()).isEqualTo(이미_신고한_신고자1.getId());
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(질문_신고1.getId());
            softAssertions.assertThat(actual.get(1).reporterDto().id()).isEqualTo(이미_신고한_신고자2.getId());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(질문_신고2.getId());
            softAssertions.assertThat(actual.get(2).reporterDto().id()).isEqualTo(이미_신고한_신고자3.getId());
            softAssertions.assertThat(actual.get(2).id()).isEqualTo(질문_신고3.getId());
        });
    }
}
