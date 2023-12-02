package com.ddang.ddang.auction.domain;

import com.ddang.ddang.auction.domain.fixture.AuctionFixture;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.user.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionTest extends AuctionFixture {

    @Test
    void 경매를_삭제한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .build();

        // when
        auction.delete();

        // then
        assertThat(auction.isDeleted()).isTrue();
    }

    @Test
    void 경매에_직거래_지역_정보를_추가한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .build();

        // when
        auction.addAuctionRegions(List.of(서울특별시_강남구_역삼동));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(auction.getAuctionRegions()).hasSize(1);
            softAssertions.assertThat(auction.getAuctionRegions()).contains(서울특별시_강남구_역삼동);
        });
    }

    @Test
    void 첫_입찰자가_시작가_보다_낮은_금액으로_입찰하는_경우_유효하지_않다는_값으로_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .description("설명")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();

        // when
        final boolean actual = auction.isInvalidFirstBidPrice(경매_시작가보다_적은_입찰_금액);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 경매_이미지_연관_관계를_세팅한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .build();

        // when
        auction.addAuctionImages(List.of(경매_이미지));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(auction.getAuctionImages()).contains(경매_이미지);
            softAssertions.assertThat(경매_이미지.getAuction()).isEqualTo(auction);
        });
    }

    @Test
    void 경매가_특정_시간을_기준으로_종료되었는지_확인한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .closingTime(LocalDateTime.now().minusDays(6))
                                       .build();

        // when
        final boolean actual = auction.isClosed(LocalDateTime.now());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 경매_마지막_입찰_정보를_업데이트한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .build();
        final Bid bid = new Bid(auction, 판매자, 유효한_입찰_금액);

        // when
        auction.updateLastBid(bid);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(auction.getLastBid()).isEqualTo(bid);
            softAssertions.assertThat(bid.getAuction()).isEqualTo(auction);
            softAssertions.assertThat(auction.getAuctioneerCount()).isEqualTo(1);
        });
    }

    @Test
    void 특정_금액이_경매의_시작가보다_작다면_유효하지_않다는_의미로_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .startPrice(new Price(1_000))
                                       .build();

        // when
        final boolean actual = auction.isInvalidFirstBidPrice(경매_시작가보다_적은_입찰_금액);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_금액이_경매의_마지막_입찰가보다_작다면_마지막_입찰가가_크다는_의미로_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .bidUnit(new BidUnit(1_000))
                                       .build();
        final Bid bid = new Bid(auction, 판매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isNextBidPriceGreaterThan(마지막_입찰가보다_적은_입찰_금액);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_회원이_경매_판매자와_일치하면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .bidUnit(new BidUnit(1_000))
                                       .seller(판매자)
                                       .build();

        // when
        final boolean actual = auction.isOwner(판매자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_회원이_경매_판매자와_일치하지_않으면_거짓을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .bidUnit(new BidUnit(1_000))
                                       .seller(판매자)
                                       .build();

        // when
        final boolean actual = auction.isOwner(구매자);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 주어진_사용자가_판매자_또는_낙찰자라면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isSellerOrWinner(구매자, LocalDateTime.now());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_판매자_또는_낙찰자가_아니라면_거짓을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isSellerOrWinner(사용자, LocalDateTime.now());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 주어진_사용자가_낙찰자라면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isWinner(구매자, LocalDateTime.now());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_낙찰자가_아니라면_거짓을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isWinner(사용자, LocalDateTime.now());

        // then
        assertThat(actual).isFalse();
    }


    @Test
    void 경매의_최종_낙찰자를_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        assertThat(actual).contains(구매자);
    }

    @Test
    void 경매가_종료되지_않았다면_최종_낙찰자는_없다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().plusDays(3L))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 입찰자가_존재하지_않는다면_최종_낙찰자가_없다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 마지막_입찰자를_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .startPrice(new Price(1_000))
                                       .bidUnit(new BidUnit(1_000))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final Optional<User> actual = auction.findLastBidder();

        // then
        assertThat(actual).contains(구매자);
    }

    @Test
    void 마지막_입찰자가_없다면_빈_Optional을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();

        // when
        final Optional<User> actual = auction.findLastBidder();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 마지막_입찰을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .startPrice(new Price(1_000))
                                       .bidUnit(new BidUnit(1_000))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final Optional<Bid> actual = auction.findLastBid();

        // then
        assertThat(actual).contains(bid);
    }

    @Test
    void 마지막_입찰이_없다면_빈_Optional을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(3L))
                                       .build();

        // when
        final Optional<Bid> actual = auction.findLastBid();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 경매를_진행중이며_입찰자가_없는_경우_UNBIDDEN을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().plusDays(2))
                                       .build();

        // when
        final AuctionStatus actual = auction.findAuctionStatus(LocalDateTime.now());

        // then
        assertThat(actual).isEqualTo(AuctionStatus.UNBIDDEN);
    }

    @Test
    void 경매가_마감되었고_입찰자가_없는_경우_FAILURE를_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(2))
                                       .build();

        // when
        final AuctionStatus actual = auction.findAuctionStatus(LocalDateTime.now());

        assertThat(actual).isEqualTo(AuctionStatus.FAILURE);
    }

    @Test
    void 경매가_진행중이며_입찰자가_있는_경우_ONGOING을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().plusDays(2))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final AuctionStatus actual = auction.findAuctionStatus(LocalDateTime.now());

        // then
        assertThat(actual).isEqualTo(AuctionStatus.ONGOING);
    }

    @Test
    void 경매가_마감되었고_입찰자가_있는_경우_SUCCESS를_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("제목")
                                       .seller(판매자)
                                       .closingTime(LocalDateTime.now().minusDays(2))
                                       .build();
        final Bid bid = new Bid(auction, 구매자, 유효한_입찰_금액);

        auction.updateLastBid(bid);

        // when
        final AuctionStatus actual = auction.findAuctionStatus(LocalDateTime.now());

        // then
        assertThat(actual).isEqualTo(AuctionStatus.SUCCESS);
    }
}
