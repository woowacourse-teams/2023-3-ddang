package com.ddang.ddang.bid.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.application.fixture.BidServiceFixture;
import com.ddang.ddang.notification.application.NotificationService;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidServiceTest extends BidServiceFixture {

    @Autowired
    BidService bidService;

    @MockBean
    NotificationService notificationService;

    @Test
    void 입찰을_등록한다() {
        // given
        given(notificationService.send(any(CreateNotificationDto.class))).willReturn("성공");

        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());

        // when
        final Long actual = bidService.create(createBidDto, 이미지_절대_경로_url);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPositive();
            softAssertions.assertThat(경매1.getLastBid().getPrice().getValue()).isEqualTo(createBidDto.bidPrice());
            softAssertions.assertThat(경매1.getAuctioneerCount()).isEqualTo(1);
            verify(notificationService).send(any());
        });
    }

    @Test
    void 마지막_입찰자와_다른_사람은_마지막_입찰액과_최소_입찰단위를_더한_금액_이상의_금액으로_입찰을_등록할_수_있다() {
        // given
        final CreateBidDto createBidDto1 = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        final CreateBidDto createBidDto2 = new CreateBidDto(경매1.getId(), 14_000, 구매자2.getId());

        bidService.create(createBidDto1, "");

        // when
        final Long actual = bidService.create(createBidDto2, "");

        // then
        assertThat(actual).isPositive();
    }

    // TODO: 2023/09/27 삭제, 입찰 등록에서 이미 검증된 로직
    @Test
    void 마지막_입찰_이후_상위_입찰자가_생기면_기존_마지막_입찰자에게_알림을_보낸다() {
        // given
        final CreateBidDto createBidDto1 = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        final CreateBidDto createBidDto2 = new CreateBidDto(경매1.getId(), 14_000, 구매자2.getId());

        bidService.create(createBidDto1, 이미지_절대_경로_url);

        // when
        bidService.create(createBidDto2, 이미지_절대_경로_url);

        // then
        verify(notificationService).send(any());
    }

    @Test
    void 첫_입찰자는_시작가를_입찰로_등록할_수_있다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 1_000, 구매자1.getId());

        // when
        final Long actual = bidService.create(createBidDto, 이미지_절대_경로_url);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 존재하지_않는_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(존재하지_않는_경매_아이디, 10_000, 구매자1.getId());

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("해당 경매를 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_사용자가_경매에_입찰하는_경우_예외가_발생한다() {
        // given

        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 10_000, 존재하지_않는_사용자_아이디);

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }

    @Test
    void 종료된_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(InvalidAuctionToBidException.class)
                .hasMessage("이미 종료된 경매입니다");
    }

    @Test
    void 삭제된_경매에_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        경매1.delete();

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(InvalidAuctionToBidException.class)
                .hasMessage("삭제된 경매입니다");
    }

    @Test
    void 판매자가_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 10_000, 판매자.getId());

        // when && then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidderException.class)
                .hasMessage("판매자는 입찰할 수 없습니다");
    }

    @Test
    void 첫_입찰자가_시작가보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), 900, 구매자1.getId());

        // when && then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @Test
    void 마지막_입찰자가_연속으로_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto1 = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        bidService.create(createBidDto1, 이미지_절대_경로_url);

        final CreateBidDto createBidDto2 = new CreateBidDto(경매1.getId(), 12_000, 구매자1.getId());

        // when && then
        assertThatThrownBy(() -> bidService.create(createBidDto2, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidderException.class)
                .hasMessage("이미 최고 입찰자입니다");
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto1 = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        bidService.create(createBidDto1, 이미지_절대_경로_url);

        final CreateBidDto createBidDto2 = new CreateBidDto(경매1.getId(), 8_000, 구매자2.getId());

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto2, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("가능 입찰액보다 낮은 금액을 입력했습니다");
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰하는_경우_예외가_발생한다() {
        // given
        final CreateBidDto createBidDto1 = new CreateBidDto(경매1.getId(), 10_000, 구매자1.getId());
        bidService.create(createBidDto1, 이미지_절대_경로_url);

        final CreateBidDto createBidDto2 = new CreateBidDto(경매1.getId(), 10_500, 구매자2.getId());

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto2, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("가능 입찰액보다 낮은 금액을 입력했습니다");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 2_100_000_001})
    void 범위_밖의_금액으로_입찰하는_경우_예외가_발생한다(final int bidPrice) {
        // given
        final CreateBidDto createBidDto = new CreateBidDto(경매1.getId(), bidPrice, 구매자1.getId());

        // when & then
        assertThatThrownBy(() -> bidService.create(createBidDto, 이미지_절대_경로_url))
                .isInstanceOf(InvalidBidPriceException.class)
                .hasMessage("입찰 금액이 잘못되었습니다");
    }

    @Test
    void 특정_경매에_대한_입찰_목록을_조회한다() {
        // when
        final List<ReadBidDto> actual = bidService.readAllByAuctionId(경매1.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).name()).isEqualTo(구매자1.getName());
            softAssertions.assertThat(actual.get(1).name()).isEqualTo(구매자2.getName());
        });
    }

    // TODO: 2023/09/27 자료구조를 네이밍에 반영해도 되나?
    // TODO: 2023/09/27 비어있다의 의미 정하기
    @Test
    void 특정_경매에_대한_입찰_내역이_없다면_빈배열을_반환한다() {
        // when
        final List<ReadBidDto> actual = bidService.readAllByAuctionId(경매1.getId());

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
