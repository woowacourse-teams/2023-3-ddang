package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class JpaBidRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaBidRepository bidRepository;

    @Test
    void 입찰을_저장한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user = new User("사용자", "이미지", 4.9);
        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        // when
        auctionRepository.save(auction);
        userRepository.save(user);
        bidRepository.save(bid);

        // then
        em.flush();
        em.clear();

        assertThat(bid.getId()).isPositive();
    }

    @Test
    void 특정_경매가_존재하는지_확인한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user = new User("사용자", "이미지", 4.9);
        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        auctionRepository.save(auction);
        userRepository.save(user);
        bidRepository.save(bid);

        em.flush();
        em.clear();

        // when
        final boolean actual = bidRepository.existsById(bid.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_경매의_입찰을_모두_조회한다() {
        // given
        final Auction auction1 = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final Auction auction2 = Auction.builder()
                                       .title("경매 상품 2")
                                       .description("이것은 경매 상품 2 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        final User user = new User("사용자", "이미지", 4.9);
        final Bid bid1 = new Bid(auction1, user, new BidPrice(10_000));
        final Bid bid2 = new Bid(auction1, user, new BidPrice(10_000));
        final Bid bid3 = new Bid(auction2, user, new BidPrice(10_000));

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        userRepository.save(user);
        bidRepository.save(bid1);
        bidRepository.save(bid2);
        bidRepository.save(bid3);

        em.flush();
        em.clear();

        // when
        final List<Bid> actual = bidRepository.findByAuctionId(auction1.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getId()).isEqualTo(bid1.getId());
            softAssertions.assertThat(actual.get(1).getId()).isEqualTo(bid2.getId());
        });
    }

    @Test
    void 특정_경매의_마지막_입찰을_조회한다() {
        // given
        final Auction auction1 = Auction.builder()
                                        .title("경매 상품 1")
                                        .description("이것은 경매 상품 1 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        final Auction auction2 = Auction.builder()
                                        .title("경매 상품 2")
                                        .description("이것은 경매 상품 2 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
        final User user = new User("사용자", "이미지", 4.9);
        final Bid bid1 = new Bid(auction1, user, new BidPrice(10_000));
        final Bid bid2 = new Bid(auction1, user, new BidPrice(12_000));
        final Bid bid3 = new Bid(auction2, user, new BidPrice(10_000));

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        userRepository.save(user);
        bidRepository.save(bid1);
        bidRepository.save(bid2);
        bidRepository.save(bid3);

        em.flush();
        em.clear();

        // when
        final Bid actual = bidRepository.findLastBidByAuctionId(auction1.getId());

        // then
        assertThat(actual.getId()).isEqualTo(bid2.getId());
    }
}
