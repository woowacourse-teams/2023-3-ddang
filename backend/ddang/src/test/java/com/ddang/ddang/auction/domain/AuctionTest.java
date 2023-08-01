package com.ddang.ddang.auction.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionTest {

    @Test
    void 경매를_삭제한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
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
                                       .title("title")
                                       .build();

        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("강남구");
        final Region thirdRegion = new Region("역삼동");

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);

        final AuctionRegion auctionRegion = new AuctionRegion(firstRegion);

        // when
        auction.addAuctionRegions(List.of(auctionRegion));

        // then
        assertThat(auction.getAuctionRegions()).hasSize(1);
    }

    @Test
    void 첫_입찰자가_시작가_보다_낮은_금액으로_입찰하는_경우_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();

        // when
        final boolean actual = auction.isInvalidFirstBidPrice(new Price(900));

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 경매_이미지_연관_관계를_세팅한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();
        final AuctionImage auctionImage = new AuctionImage("image.png", "image.png");

        // when
        auction.addAuctionImages(List.of(auctionImage));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(auction.getAuctionImages()).isNotEmpty();
            softAssertions.assertThat(auctionImage.getAuction()).isNotNull();
        });
    }

    @Test
    void 경매_판매자_연관_관계를_세팅한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();
        final User seller = new User("seller", "https://profile.com", 3.5d);

        // when
        auction.addSeller(seller);

        // then
        assertThat(auction.getSeller()).isNotNull();
    }

    @Test
    void 경매가_특정_시간을_기준으로_종료되었는지_확인한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
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
                                       .title("title")
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        final Bid bid = new Bid(auction, user, new Price(10_000));

        // when
        auction.updateLastBidPrice(bid);

        // then
        assertThat(auction.getLastBid()).isEqualTo(bid);
    }

    @Test
    void 특정_금액이_경매의_시작가보다_작다면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .startPrice(new Price(1_000))
                                       .build();

        // when
        final boolean actual = auction.isInvalidFirstBidPrice(new Price(900));

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_금액이_경매의_마지막_입찰가보다_작다면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .bidUnit(new BidUnit(1_000))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);
        final Bid bid = new Bid(auction, user, new Price(10_000));

        auction.updateLastBidPrice(bid);

        // when
        final boolean actual = auction.isSmallerThanNextBidPrice(new Price(9_000));

        // then
        assertThat(actual).isTrue();
    }
}
