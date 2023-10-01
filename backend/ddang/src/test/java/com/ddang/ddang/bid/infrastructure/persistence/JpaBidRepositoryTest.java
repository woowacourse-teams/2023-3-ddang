package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.infrastructure.persistence.fixture.JpaBidRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaBidRepositoryTest extends JpaBidRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaBidRepository bidRepository;

    @Test
    void 입찰을_저장한다() {
        // given
        final Bid bid = new Bid(경매1, 입찰자1, 입찰액);

        // when
        final Bid actual = bidRepository.save(bid);

        // then
        em.flush();
        em.clear();

        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 특정_경매가_존재하는지_확인한다() {
        // when
        final boolean actual = bidRepository.existsById(경매1의_입찰1.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_경매의_입찰을_모두_조회한다() {
        // when
        final List<Bid> actual = bidRepository.findByAuctionIdOrderByIdAsc(경매1.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(경매1의_입찰1.getId());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(경매1의_입찰2겸_마지막_입찰.getId());
        });
    }

    @Test
    void 특정_경매의_마지막_입찰을_조회한다() {
        // when
        final Bid actual = bidRepository.findLastBidByAuctionId(경매1.getId());

        // then
        assertThat(actual.getId()).isEqualTo(경매1의_입찰2겸_마지막_입찰.getId());
    }
}
