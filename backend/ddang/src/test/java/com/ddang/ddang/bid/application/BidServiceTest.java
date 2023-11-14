package com.ddang.ddang.bid.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.application.fixture.BidServiceFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.notification.application.NotificationService;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@IsolateDatabase
@RecordApplicationEvents
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidServiceTest extends BidServiceFixture {

    @Autowired
    BidService bidService;

    @MockBean
    NotificationService notificationService;

    @Autowired
    ApplicationEvents events;

    @Test
    void 입찰을_등록한다() {
        // when
        final Long actual = bidService.create(입찰_요청_dto, 이미지_절대_url);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(입찰_내역이_없는_경매.getLastBid().getPrice().getValue()).isEqualTo(입찰_요청_dto.bidPrice());
            softAssertions.assertThat(입찰_내역이_없는_경매.getAuctioneerCount()).isEqualTo(1);
        });
    }

    @Test
    void 마지막_입찰자와_다른_사람은_마지막_입찰액과_최소_입찰단위를_더한_금액_이상의_금액으로_입찰을_등록할_수_있다() throws FirebaseMessagingException {
        // given
        given(notificationService.send(any(CreateNotificationDto.class))).willReturn(알림_성공);

        // when
        final Long actual = bidService.create(입찰_내역이_하나_존재하는_경매에_대한_입찰_요청_dto, 이미지_절대_url);
        final long eventActual = events.stream(BidNotificationEvent.class).count();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(입찰_내역이_하나_있던_경매.getLastBid().getPrice().getValue())
                          .isEqualTo(입찰_내역이_하나_존재하는_경매에_대한_입찰_요청_dto.bidPrice());
            softAssertions.assertThat(입찰_내역이_하나_있던_경매.getAuctioneerCount()).isEqualTo(2);
            softAssertions.assertThat(eventActual).isEqualTo(1);
        });
    }

    @Test
    void 첫_입찰자는_시작가를_입찰로_등록할_수_있다() {
        // when
        final Long actual = bidService.create(첫입찰자가_시작가로_입찰_요청_dto, 이미지_절대_url);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_경매에_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(존재하지_않는_경매_아이디에_대한_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(AuctionNotFoundException.class);
    }

    @Test
    void 존재하지_않는_사용자가_경매에_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(존재하지_않는_사용자_아이디를_통한_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 종료된_경매에_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(종료된_경매에_대한_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidAuctionToBidException.class)
                .hasMessage("이미 종료된 경매입니다");
    }

    @Test
    void 삭제된_경매에_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(삭제된_경매에_대한_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(AuctionNotFoundException.class);
    }

    @Test
    void 판매자가_입찰하는_경우_예외가_발생한다() {
        // when && then
        assertThatThrownBy(() -> bidService.create(판매자가_본인_경매에_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidderException.class)
                .hasMessage("판매자는 입찰할 수 없습니다");
    }

    @Test
    void 첫_입찰자가_시작가보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // when && then
        assertThatThrownBy(() -> bidService.create(첫입찰시_시작가보다_낮은_입찰액으로_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @Test
    void 마지막_입찰자가_연속으로_입찰하는_경우_예외가_발생한다() {
        // when && then
        assertThatThrownBy(() -> bidService.create(동일한_사용자가_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidderException.class)
                .hasMessage("이미 최고 입찰자입니다");
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(이전_입찰액보다_낮은_입찰액으로_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("가능 입찰액보다 낮은 금액을 입력했습니다");
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> bidService.create(최소_입찰단위를_더한_금액보다_낮은_입찰액으로_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("가능 입찰액보다 낮은 금액을 입력했습니다");
    }

    @ParameterizedTest
    @MethodSource("provideBidRequestWithOutOfBoundBidPrice")
    void 범위_밖의_금액으로_입찰하는_경우_예외가_발생한다(final CreateBidDto 범위_밖의_금액으로_입찰_요청_dto) {
        // when & then
        assertThatThrownBy(() -> bidService.create(범위_밖의_금액으로_입찰_요청_dto, 이미지_절대_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    private static Stream<CreateBidDto> provideBidRequestWithOutOfBoundBidPrice() {
        return Stream.of(범위_밖의_금액으로_입찰_요청_dto1, 범위_밖의_금액으로_입찰_요청_dto2);
    }

    @Test
    void 특정_경매에_대한_입찰_목록을_조회한다() {
        // when
        final List<ReadBidDto> actual = bidService.readAllByAuctionId(경매1.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).name()).isEqualTo(입찰자1.getName());
            softAssertions.assertThat(actual.get(1).name()).isEqualTo(입찰자2.getName());
        });
    }

    @Test
    void 특정_경매에_대한_입찰_내역이_없다면_빈배열을_반환한다() {
        // when
        final List<ReadBidDto> actual = bidService.readAllByAuctionId(입찰_내역이_없는_경매.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 입찰을_조회하려는_경매가_존재하지_않는_경우_예외를_반환한다() {
        // when & then
        assertThatThrownBy(() -> bidService.readAllByAuctionId(존재하지_않는_경매_아이디))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }
}
