package com.ddang.ddang.auction.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.JpaAuctionRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
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

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Test
    void 경매를_저장한다() {
        // when
        final Auction actual = auctionRepository.save(저장하기_전_경매_엔티티);

        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 지정한_아이디에_대한_경매를_조회한다() {
        // when
        final Optional<Auction> actual = auctionRepository.findById(저장된_경매_엔티티.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getId()).isEqualTo(저장된_경매_엔티티.getId());
            softAssertions.assertThat(actual.get().getTitle()).isEqualTo(저장된_경매_엔티티.getTitle());
            softAssertions.assertThat(actual.get().getDescription()).isEqualTo(저장된_경매_엔티티.getDescription());
            softAssertions.assertThat(actual.get().getBidUnit()).isEqualTo(저장된_경매_엔티티.getBidUnit());
            softAssertions.assertThat(actual.get().getStartPrice()).isEqualTo(저장된_경매_엔티티.getStartPrice());
            softAssertions.assertThat(actual.get().getClosingTime()).isEqualTo(저장된_경매_엔티티.getClosingTime());
        });
    }
}
