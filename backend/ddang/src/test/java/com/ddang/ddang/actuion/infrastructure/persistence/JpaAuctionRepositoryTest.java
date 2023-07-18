package com.ddang.ddang.actuion.infrastructure.persistence;

import com.ddang.ddang.actuion.domain.Auction;
import com.ddang.ddang.actuion.domain.Price;
import com.ddang.ddang.configuration.JpaConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
@Import(JpaConfiguration.class)
class JpaAuctionRepositoryTest {

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
                                       .startBidPrice(new Price(1000))
                                       .closingTime(LocalDateTime.now())
                                       .build();

        // when
        auctionRepository.save(auction);

        // then
        em.flush();
        em.clear();

        assertThat(auction.getId())
                .isPositive();
    }
}
