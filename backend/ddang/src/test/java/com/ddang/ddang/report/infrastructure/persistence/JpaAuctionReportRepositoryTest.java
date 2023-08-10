package com.ddang.ddang.report.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaAuctionReportRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaAuctionReportRepository auctionReportRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 경매_신고를_저장한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final AuctionReport auctionReport = new AuctionReport(user, auction, "신고합니다");

        auctionRepository.save(auction);
        userRepository.save(user);

        // when
        auctionReportRepository.save(auctionReport);

        // then
        em.flush();
        em.clear();

        assertThat(auctionReport.getId()).isPositive();
    }

    @Test
    void 특정_경매_아이디와_신고자_아이디가_동일한_레코드가_존재하면_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user = User.builder()
                                .name("사용자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final AuctionReport auctionReport = new AuctionReport(user, auction, "신고합니다");

        auctionRepository.save(auction);
        userRepository.save(user);
        auctionReportRepository.save(auctionReport);

        em.flush();
        em.clear();

        // when
        final boolean actual = auctionReportRepository.existsByAuctionIdAndReporterId(auction.getId(), user.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_경매_아이디와_신고자_아이디가_동일한_레코드가_존재하지_않는다면_거짓을_반환한다() {
        // given
        final long invalidAuctionId = -9999L;
        final long invalidUserId = -9999L;

        // when
        final boolean actual = auctionReportRepository.existsByAuctionIdAndReporterId(invalidAuctionId, invalidUserId);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 전체_경매_신고_목록을_조회한다() {
        // given
        final User seller = User.builder()
                                .name("판매자")
                                .profileImage("profile.png")
                                .reliability(4.7d)
                                .oauthId("12345")
                                .build();
        final Auction auction = Auction.builder()
                                       .seller(seller)
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user1 = User.builder()
                               .name("사용자1")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
        final User user2 = User.builder()
                               .name("사용자2")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();
        final User user3 = User.builder()
                               .name("사용자3")
                               .profileImage("profile.png")
                               .reliability(4.7d)
                               .oauthId("12348")
                               .build();
        final AuctionReport auctionReport1 = new AuctionReport(user1, auction, "신고합니다");
        final AuctionReport auctionReport2 = new AuctionReport(user2, auction, "신고합니다");
        final AuctionReport auctionReport3 = new AuctionReport(user3, auction, "신고합니다");

        userRepository.save(seller);
        auctionRepository.save(auction);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        auctionReportRepository.save(auctionReport1);
        auctionReportRepository.save(auctionReport2);
        auctionReportRepository.save(auctionReport3);

        em.flush();
        em.clear();

        // when
        final List<AuctionReport> actual = auctionReportRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getReporter()).isEqualTo(user1);
            softAssertions.assertThat(actual.get(0).getAuction()).isEqualTo(auction);
            softAssertions.assertThat(actual.get(1).getReporter()).isEqualTo(user2);
            softAssertions.assertThat(actual.get(1).getAuction()).isEqualTo(auction);
            softAssertions.assertThat(actual.get(2).getReporter()).isEqualTo(user3);
            softAssertions.assertThat(actual.get(2).getAuction()).isEqualTo(auction);
        });
    }
}
