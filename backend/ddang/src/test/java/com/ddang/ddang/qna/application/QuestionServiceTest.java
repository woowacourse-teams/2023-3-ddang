package com.ddang.ddang.qna.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.qna.application.dto.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.ReadQnasDto;
import com.ddang.ddang.qna.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.qna.application.exception.InvalidQuestionerException;
import com.ddang.ddang.qna.application.exception.QuestionNotFoundException;
import com.ddang.ddang.qna.application.fixture.QuestionServiceFixture;
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
class QuestionServiceTest extends QuestionServiceFixture {

    @Autowired
    QuestionService questionService;

    @Test
    void 질문을_등록한다() {
        // when
        final Long actual = questionService.create(경매_질문_등록_요청_dto);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(존재하지_않는_사용자가_경매_질문_등록_요청_dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(존재하지_않는_경매_질문_등록_요청_dto))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 이미_종료된_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(종료된_경매_질문_등록_요청_dto))
                .isInstanceOf(InvalidAuctionToAskQuestionException.class)
                .hasMessage("이미 종료된 경매입니다.");
    }

    @Test
    void 삭제된_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(삭제된_경매_질문_등록_요청_dto))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 판매자가_본인_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(판매자가_본인_경매_질문_등록_요청_dto))
                .isInstanceOf(InvalidQuestionerException.class)
                .hasMessage("경매 등록자는 질문할 수 없습니다.");
    }

    @Test
    void 경매_아이디를_통해_질문과_답변을_모두_조회한다() {
        // when
        final ReadQnasDto actual = questionService.readAllByAuctionId(질문_3개_답변_2개가_존재하는_경매_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadQnaDto> questionAndAnswerDtos = actual.readQnaDtos();
            softAssertions.assertThat(questionAndAnswerDtos).hasSize(3);

            final ReadQnaDto 첫번째_질문 = questionAndAnswerDtos.get(0);
            softAssertions.assertThat(첫번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto1);
            softAssertions.assertThat(첫번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto1);

            final ReadQnaDto 두번째_질문 = questionAndAnswerDtos.get(1);
            softAssertions.assertThat(두번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto2);
            softAssertions.assertThat(두번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto2);

            final ReadQnaDto 세번째_질문 = questionAndAnswerDtos.get(2);
            softAssertions.assertThat(세번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto3);
            softAssertions.assertThat(세번째_질문.readAnswerDto()).isNull();
        });
    }

    @Test
    void 존재하지_않는_경매_아이디를_통해_질문과_답변을_모두_조회할시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.readAllByAuctionId(존재하지_않는_경매_아이디))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 경매를_삭제한다() {
        // when
        questionService.deleteById(질문_아이디, 사용자_아이디);

        // then
        assertThat(질문.isDeleted()).isTrue();
    }

    @Test
    void 존재하지_않는_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(존재하지_않는_질문_아이디, 사용자_아이디))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessage("해당 질문을 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_사용자가_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(질문_아이디, 존재하지_않는_사용자_아이디))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 질문_작성자가_아닌_사용자가_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(질문_아이디, 질문하지_않은_사용자.getId()))
                .isInstanceOf(UserForbiddenException.class)
                .hasMessage("삭제할 권한이 없습니다.");
    }
}
