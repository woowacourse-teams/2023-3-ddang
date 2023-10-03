package com.ddang.ddang.review.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.review.application.dto.ReadReviewDetailDto;
import com.ddang.ddang.review.application.dto.ReadReviewDto;
import com.ddang.ddang.review.application.exception.AlreadyReviewException;
import com.ddang.ddang.review.application.exception.InvalidUserToReview;
import com.ddang.ddang.review.application.fixture.ReviewServiceFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReviewServiceTest extends ReviewServiceFixture {

    @Autowired
    ReviewService reviewService;

    @Test
    void 평가를_등록한고_평가_상대의_신뢰도를_갱신한다() {
        // when
        final Long actual = reviewService.create(구매자에_대한_평가_등록을_위한_DTO);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(구매자.getReliability()).isEqualTo(구매자가_새로운_평가_점수를_받고난_후의_신뢰도);
        });
    }

    @Test
    void 연관된_경매를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewService.create(존재하지_않는_경매와_연관된_평가를_등록하려는_DTO))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 평가_작성자를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewService.create(존재하지_않는_사용자가_평가를_등록하려는_DTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("작성자 정보를 찾을 수 없습니다.");
    }

    @Test
    void 평가_상대를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewService.create(존재하지_않는_사용자를_평가하려는_DTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("평가 상대의 정보를 찾을 수 없습니다.");
    }

    @Test
    void 경매의_판매자나_낙찰자가_아닌_사용자일_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewService.create(경매_참여자가_아닌_사용자가_평가를_등록하려는_DTO))
                .isInstanceOf(InvalidUserToReview.class)
                .hasMessage("경매의 판매자 또는 최종 낙찰자만 평가가 가능합니다.");
    }

    @Test
    void 이미_평가했는데_평가를_등록한다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewService.create(이미_평가한_경매와_연관된_평가를_또_등록하려는_DTO))
                .isInstanceOf(AlreadyReviewException.class)
                .hasMessage("이미 평가하였습니다.");
    }

    @Test
    void 지정한_아이디가_평가_대상인_평가_목록을_최신순으로_조회한다() {
        // when
        final List<ReadReviewDto> actual = reviewService.readAllByTargetId(구매자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(구매자가_판매자2에게_받은_평가.getId());
            softAssertions.assertThat(actual.get(0).writer().id()).isEqualTo(판매자2.getId());
            softAssertions.assertThat(actual.get(0).content()).isEqualTo(구매자가_판매자2에게_받은_평가.getContent());
            softAssertions.assertThat(actual.get(0).score()).isEqualTo(구매자가_판매자2에게_받은_평가.getScore());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(구매자가_판매자1에게_받은_평가.getId());
            softAssertions.assertThat(actual.get(1).writer().id()).isEqualTo(판매자1.getId());
            softAssertions.assertThat(actual.get(1).content()).isEqualTo(구매자가_판매자1에게_받은_평가.getContent());
            softAssertions.assertThat(actual.get(1).score()).isEqualTo(구매자가_판매자1에게_받은_평가.getScore());
        });
    }

    @Test
    void 지정한_경매_아이디와_작성자_아이디가_해당하는_평가가_존재한다면_dto에_넣어_반환한다() {
        // when
        final ReadReviewDetailDto actual = reviewService.read(판매자1.getId(), 판매자1이_평가한_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.score()).isEqualTo(구매자가_판매자1에게_받은_평가.getScore());
            softAssertions.assertThat(actual.content()).isEqualTo(구매자가_판매자1에게_받은_평가.getContent());
        });
    }

    @Test
    void 지정한_경매_아이디와_작성자_아이디가_해당하는_평가가_존재하지_않는다면_dto의_필드가_null이다() {
        // when
        final ReadReviewDetailDto actual = reviewService.read(평가_안한_경매_판매자.getId(), 평가_안한_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.score()).isNull();
            softAssertions.assertThat(actual.content()).isNull();
        });
    }
}
