package com.ddang.ddang.questionandanswer.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswersDto;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidQuestionerException;
import com.ddang.ddang.questionandanswer.application.fixture.QuestionServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.SoftAssertions;
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
                .isInstanceOf(InvalidAuctionToAskQuestionException.class)
                .hasMessage("삭제된 경매입니다.");
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
        final ReadQuestionAndAnswersDto actual = questionService.readAllByAuctionId(질문_3개_답변_2개가_존재하는_경매_아이디);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadQuestionAndAnswerDto> questionAndAnswerDtos = actual.readQuestionAndAnswerDtos();
            softAssertions.assertThat(questionAndAnswerDtos).hasSize(3);

            final ReadQuestionAndAnswerDto 첫번째_질문 = questionAndAnswerDtos.get(0);
            softAssertions.assertThat(첫번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto1);
            softAssertions.assertThat(첫번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto1);

            final ReadQuestionAndAnswerDto 두번째_질문 = questionAndAnswerDtos.get(1);
            softAssertions.assertThat(두번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto2);
            softAssertions.assertThat(두번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto2);

            final ReadQuestionAndAnswerDto 세번째_질문 = questionAndAnswerDtos.get(2);
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
}
