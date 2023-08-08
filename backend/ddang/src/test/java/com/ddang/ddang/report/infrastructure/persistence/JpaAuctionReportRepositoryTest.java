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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

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
        final User user = new User("사용자", "이미지", 4.9);
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
        final User user = new User("사용자", "이미지", 4.9);
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
}
