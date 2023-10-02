package com.ddang.ddang.auction.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.JpaAuctionRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaAuctionRepositoryTest extends JpaAuctionRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Test
    void 경매를_저장한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();

        // when
        auctionRepository.save(auction);

        // then
        em.flush();
        em.clear();

        assertThat(auction.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_경매를_조회한다() {
        // given
        final Auction expected = Auction.builder()
                                        .title("경매 상품 1")
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(시간.atZone(위치).toLocalDateTime())
                                        .build();

        auctionRepository.save(expected);

        em.flush();
        em.clear();

        // when
        final Optional<Auction> actual = auctionRepository.findById(expected.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isEqualTo(expected.getId());
            softAssertions.assertThat(actual.get().getTitle()).isEqualTo(expected.getTitle());
            softAssertions.assertThat(actual.get().getDescription()).isEqualTo(expected.getDescription());
            softAssertions.assertThat(actual.get().getBidUnit()).isEqualTo(expected.getBidUnit());
            softAssertions.assertThat(actual.get().getStartPrice()).isEqualTo(expected.getStartPrice());
            softAssertions.assertThat(actual.get().getClosingTime()).isEqualTo(expected.getClosingTime());
        });
    }
}
