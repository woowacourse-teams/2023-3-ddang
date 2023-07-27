package com.ddang.ddang.auction.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class QuerydslAuctionRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaRegionRepository regionRepository;

    QuerydslAuctionRepository querydslAuctionRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslAuctionRepository = new QuerydslAuctionRepositoryImpl(queryFactory);
    }

    @Test
    void 지정한_아이디에_대한_경매를_조회한다() {
        // given
        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("강남구");
        final Region thirdRegion = new Region("개포1동");

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);

        regionRepository.save(firstRegion);
        final AuctionRegion auctionRegion = new AuctionRegion(thirdRegion);

        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();
        auction.addAuctionRegion(auctionRegion);

        auctionRepository.save(auction);

        em.flush();
        em.clear();

        // when
        final Optional<Auction> actual = querydslAuctionRepository.findAuctionWithRegionsById(auction.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();

            final Auction actualAuction = actual.get();
            softAssertions.assertThat(actualAuction.getTitle()).isEqualTo(auction.getTitle());
            softAssertions.assertThat(actualAuction.getId()).isEqualTo(auction.getId());
            softAssertions.assertThat(actualAuction.getAuctionRegions()).hasSize(1);

            final Region actualThirdRegion = actualAuction.getAuctionRegions().get(0).getThirdRegion();
            softAssertions.assertThat(actualThirdRegion.getName()).isEqualTo(thirdRegion.getName());

            final Region actualSecondRegion = actualThirdRegion.getSecondRegion();
            softAssertions.assertThat(actualSecondRegion.getName()).isEqualTo(secondRegion.getName());

            final Region actualFirstRegion = actualSecondRegion.getFirstRegion();
            softAssertions.assertThat(actualFirstRegion.getName()).isEqualTo(firstRegion.getName());
        });
    }

    @Test
    void 지정한_아이디에_해당하는_경매가_없는_경우_빈_Optional을_조회한다() {
        // given
        final Long invalidId = -999L;

        // when
        final Optional<Auction> actual = querydslAuctionRepository.findAuctionWithRegionsById(invalidId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 첫번째_페이지의_경매_목록을_조회한다() {
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
        final Auction auction3 = Auction.builder()
                                        .title("경매 상품 3")
                                        .description("이것은 경매 상품 3 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        auctionRepository.save(auction3);

        em.flush();
        em.clear();

        // when
        final List<Auction> actual = auctionRepository.findAuctionsAllByLastAuctionId(null, 1);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getTitle()).isEqualTo(auction3.getTitle());
        });
    }

    @Test
    void 두번째_페이지의_경매_목록을_조회한다() {
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
        final Auction auction3 = Auction.builder()
                                        .title("경매 상품 3")
                                        .description("이것은 경매 상품 3 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        auctionRepository.save(auction3);

        em.flush();
        em.clear();

        // when
        final List<Auction> actual = auctionRepository.findAuctionsAllByLastAuctionId(auction3.getId(), 1);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getTitle()).isEqualTo(auction2.getTitle());
        });
    }

    @Test
    void 두번째_페이지의_삭제된_경매를_제외한_목록을_조회한다() {
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
        final Auction auction3 = Auction.builder()
                                        .title("경매 상품 3")
                                        .description("이것은 경매 상품 3 입니다.")
                                        .bidUnit(new BidUnit(1_000))
                                        .startPrice(new Price(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);
        auctionRepository.save(auction3);

        auction2.delete();

        em.flush();
        em.clear();

        // when
        final List<Auction> actual = auctionRepository.findAuctionsAllByLastAuctionId(auction3.getId(), 1);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0).getTitle()).isEqualTo(auction1.getTitle());
        });
    }
}
