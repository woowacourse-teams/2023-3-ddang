package com.ddang.ddang.auction.domain;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class AuctionTest {

    @Autowired
    JpaUserRepository userRepository;

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
        final boolean actual = auction.isInvalidFirstBidPrice(new BidPrice(900));

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
        final User user = User.builder()
                              .name("회원")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        // when
        auction.updateLastBid(bid);

        // then
        assertThat(auction.getLastBid()).isEqualTo(bid);
        assertThat(auction.getAuctioneerCount()).isEqualTo(1);
    }

    @Test
    void 특정_금액이_경매의_시작가보다_작다면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .startPrice(new Price(1_000))
                                       .build();

        // when
        final boolean actual = auction.isInvalidFirstBidPrice(new BidPrice(900));

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
        final User user = User.builder()
                              .name("회원")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        auction.updateLastBid(bid);

        // when
        final boolean actual = auction.isNextBidPriceGreaterThan(new BidPrice(9_000));

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_회원이_경매_판매자와_일치하면_참을_반환한다() {
        // given
        final User seller = User.builder()
                                .name("회원")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .bidUnit(new BidUnit(1_000))
                                       .seller(seller)
                                       .build();

        // when
        final boolean actual = auction.isOwner(seller);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_회원이_경매_판매자와_일치하지_않으면_거짓을_반환한다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .bidUnit(new BidUnit(1_000))
                                       .seller(seller)
                                       .build();
        final User user = User.builder()
                              .name("회원2")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        // when
        final boolean actual = auction.isOwner(user);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 주어진_사용자가_낙찰자라면_참을_반환한다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User winner = User.builder()
                                .name("회원2")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        userRepository.save(winner);

        final LocalDateTime pastTime = LocalDateTime.now().minusDays(3L);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .closingTime(pastTime)
                                       .build();
        auction.updateLastBid(new Bid(auction, winner, new BidPrice(10_000)));

        // when
        final boolean actual = auction.isWinner(winner, LocalDateTime.now());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 주어진_사용자가_낙찰자가_아니라면_거짓을_반환한다() {
        // given

        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User winner = User.builder()
                                .name("회원2")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();
        final User stranger = User.builder()
                                  .name("회원3")
                                  .profileImage("profile.png")
                                  .reliability(4.7d)
                                  .oauthId("12347")
                                  .build();

        userRepository.save(seller);
        userRepository.save(winner);
        userRepository.save(stranger);

        final LocalDateTime pastTime = LocalDateTime.now().minusDays(3L);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .closingTime(pastTime)
                                       .build();
        auction.updateLastBid(new Bid(auction, winner, new BidPrice(10_000)));

        // when
        final boolean actual = auction.isWinner(stranger, LocalDateTime.now());

        // then
        assertThat(actual).isFalse();
    }


    @Test
    void 경매의_최종_낙찰자를_반환한다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User winner = User.builder()
                                .name("회원2")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        userRepository.save(winner);

        final LocalDateTime pastTime = LocalDateTime.now().minusDays(3L);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .closingTime(pastTime)
                                       .build();
        auction.updateLastBid(new Bid(auction, winner, new BidPrice(10_000)));

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(actual).isPresent();
            assertThat(actual).contains(winner);
        });
    }

    @Test
    void 경매가_종료되지_않았다면_최종_낙찰자가_없다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final User winner = User.builder()
                                .name("회원2")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12346")
                                .build();

        userRepository.save(seller);
        userRepository.save(winner);

        final LocalDateTime futureTime = LocalDateTime.now().plusDays(3L);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .closingTime(futureTime)
                                       .build();
        auction.updateLastBid(new Bid(auction, winner, new BidPrice(10_000)));

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 입찰자가_존재하지_않는다면_최종_낙찰자가_없다() {
        // given
        final User seller = User.builder()
                                .name("회원1")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        userRepository.save(seller);

        final LocalDateTime pastTime = LocalDateTime.now().minusDays(3L);

        final Auction auction = Auction.builder()
                                       .title("경매")
                                       .seller(seller)
                                       .closingTime(pastTime)
                                       .build();

        // when
        final Optional<User> actual = auction.findWinner(LocalDateTime.now());

        // then
        assertThat(actual).isEmpty();
    }
}
