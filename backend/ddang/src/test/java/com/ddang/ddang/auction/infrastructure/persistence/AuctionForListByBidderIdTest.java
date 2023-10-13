package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.AuctionForListByBidderIdFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionForListByBidderIdTest extends AuctionForListByBidderIdFixture {

    QuerydslAuctionRepository querydslAuctionRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslAuctionRepository = new QuerydslAuctionRepository(queryFactory);
    }

    @Nested
    class 참여한_경매가_7개인_사용자_테스트 {

        @Test
        void 페이지_크기_3_첫번째_페이지_요청_테스트() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                    참여한_경매가_7개인_사용자.getId(),
                    PageRequest.of(0, 페이지_크기_3)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(첫번째_페이지_인덱스_0);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(첫번째_페이지_인덱스_1);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(첫번째_페이지_인덱스_2);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 페이지_크기_3_두번째_페이지_요청_테스트() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                    참여한_경매가_7개인_사용자.getId(),
                    PageRequest.of(1, 페이지_크기_3)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(두번째_페이지_인덱스_0);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(두번째_페이지_인덱스_1);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(두번째_페이지_인덱스_2);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 페이지_크기_3_세번째_페이지_요청_테스트() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                    참여한_경매가_7개인_사용자.getId(),
                    PageRequest.of(2, 페이지_크기_3)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(1);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(세번째_페이지_인덱스_0);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        @Test
        void 페이지_크기_3_네번째_페이지_요청_테스트() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                    참여한_경매가_7개인_사용자.getId(),
                    PageRequest.of(3, 페이지_크기_3)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).isEmpty();
                softAssertions.assertThat(actual.getContent()).isEmpty();
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }
    }

    @Nested
    class 참여한_경매가_없는_사용자_테스트 {

        @Test
        void 페이지_크기_3_첫번째_페이지_요청_테스트() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                    참여한_경매가_없는_사용자.getId(),
                    PageRequest.of(0, 페이지_크기_3)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).isEmpty();
                softAssertions.assertThat(actual.getContent()).isEmpty();
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }
    }
}
