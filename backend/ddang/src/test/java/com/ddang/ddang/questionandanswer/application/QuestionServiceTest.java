package com.ddang.ddang.questionandanswer.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidQuestionerException;
import com.ddang.ddang.questionandanswer.application.fixture.QuestionServiceFixture;
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
}
