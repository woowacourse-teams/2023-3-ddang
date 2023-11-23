package com.ddang.ddang.qna.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.qna.application.dto.response.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.response.ReadQnasDto;
import com.ddang.ddang.qna.application.event.QuestionNotificationEvent;
import com.ddang.ddang.qna.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.qna.application.exception.InvalidQuestionerException;
import com.ddang.ddang.qna.infrastructure.exception.QuestionNotFoundException;
import com.ddang.ddang.qna.application.fixture.QuestionServiceFixture;
import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuestionServiceTest extends QuestionServiceFixture {

    @Autowired
    QuestionService questionService;

    @Autowired
    ApplicationEvents events;

    @Test
    void 질문을_등록한다() {
        // when
        final Long actual = questionService.create(경매_질문_등록_요청_dto, 이미지_절대_경로);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_사용자가_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(존재하지_않는_사용자가_경매_질문_등록_요청_dto, 이미지_절대_경로))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 존재하지_않는_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(존재하지_않는_경매_질문_등록_요청_dto, 이미지_절대_경로))
                .isInstanceOf(AuctionNotFoundException.class);
    }

    @Test
    void 이미_종료된_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(종료된_경매_질문_등록_요청_dto, 이미지_절대_경로))
                .isInstanceOf(InvalidAuctionToAskQuestionException.class)
                .hasMessage("이미 종료된 경매입니다.");
    }

    @Test
    void 삭제된_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(삭제된_경매_질문_등록_요청_dto, 이미지_절대_경로))
                .isInstanceOf(AuctionNotFoundException.class);
    }

    @Test
    void 판매자가_본인_경매에_질문하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.create(판매자가_본인_경매_질문_등록_요청_dto, 이미지_절대_경로))
                .isInstanceOf(InvalidQuestionerException.class)
                .hasMessage("경매 등록자는 질문할 수 없습니다.");
    }

    @Test
    void 경매_아이디를_통해_질문과_답변을_모두_조회한다() {
        // when
        final ReadQnasDto actual = questionService.readAllByAuctionId(질문_3개_답변_2개가_존재하는_경매_아이디, 두번째_질문을_작성한_사용자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            final List<ReadQnaDto> questionAndAnswerDtos = actual.readQnaDtos();
            softAssertions.assertThat(questionAndAnswerDtos).hasSize(3);

            final ReadQnaDto 첫번째_질문 = questionAndAnswerDtos.get(0);
            softAssertions.assertThat(첫번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto1);
            softAssertions.assertThat(첫번째_질문.readQuestionDto().isQuestioner()).isFalse();
            softAssertions.assertThat(첫번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto1);

            final ReadQnaDto 두번째_질문 = questionAndAnswerDtos.get(1);
            softAssertions.assertThat(두번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto2);
            softAssertions.assertThat(두번째_질문.readQuestionDto().isQuestioner()).isTrue();
            softAssertions.assertThat(두번째_질문.readAnswerDto()).isEqualTo(답변_정보_dto2);

            final ReadQnaDto 세번째_질문 = questionAndAnswerDtos.get(2);
            softAssertions.assertThat(세번째_질문.readQuestionDto()).isEqualTo(질문_정보_dto3);
            softAssertions.assertThat(세번째_질문.readQuestionDto().isQuestioner()).isFalse();
            softAssertions.assertThat(세번째_질문.readAnswerDto()).isNull();
        });
    }

    @Test
    void 존재하지_않는_경매_아이디를_통해_질문과_답변을_모두_조회할시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.readAllByAuctionId(존재하지_않는_경매_아이디, 질문하지_않은_사용자.getId()))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 질문을_삭제한다() {
        // when
        questionService.deleteById(질문.getId(), 질문자.getId());

        // then
        assertThat(질문.isDeleted()).isTrue();
    }

    @Test
    void 존재하지_않는_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(존재하지_않는_질문_아이디, 질문자.getId()))
                .isInstanceOf(QuestionNotFoundException.class);
    }

    @Test
    void 존재하지_않는_사용자가_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(질문.getId(), 존재하지_않는_사용자_아이디))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 질문_작성자가_아닌_사용자가_질문_삭제시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> questionService.deleteById(질문.getId(), 질문하지_않은_사용자.getId()))
                .isInstanceOf(UserForbiddenException.class)
                .hasMessage("삭제할 권한이 없습니다.");
    }

    @Test
    void 질문이_생성되면_판매자에게_알림을_보낸다() {
        // when
        questionService.create(경매_질문_등록_요청_dto, 이미지_절대_경로);
        final long actual = events.stream(QuestionNotificationEvent.class).count();

        // then
        assertThat(actual).isEqualTo(1);
    }
}
