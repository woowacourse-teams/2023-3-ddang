package com.ddang.ddang.auction.infrastructure.persistence.list;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.list.AuctionForListSearchByTitleAndSortByAuctioneerCountFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
class AuctionForListSearchByTitleAndSortByAuctioneerCountTest extends AuctionForListSearchByTitleAndSortByAuctioneerCountFixture {

    QuerydslAuctionRepository querydslAuctionRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory queryFactory) {
        querydslAuctionRepository = new QuerydslAuctionRepository(queryFactory);
    }

    @Test
    void 첫번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(0, 페이지_크기_3, 참여_인원순_정렬)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(첫번째_페이지_인덱스_0_참여자_7_2일_후_마감_id_4);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(첫번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_15);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(첫번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_14);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    @Test
    void 두번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(1, 페이지_크기_3, 참여_인원순_정렬)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(두번째_페이지_인덱스_0_참여자_6_4일_후_마감_id_12);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(두번째_페이지_인덱스_1_참여자_6_4일_후_마감_id_11);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(두번째_페이지_인덱스_2_참여자_6_4일_후_마감_id_10);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    @Test
    void 세번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(2, 페이지_크기_3, 참여_인원순_정렬)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(세번째_페이지_인덱스_0_참여자_4_3일_후_마감_id_3);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(세번째_페이지_인덱스_1_참여자_1_4일_후_마감_id_2);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(세번째_페이지_인덱스_2_참여자_6_4일_전_마감_id_16);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    @Test
    void 네번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(3, 페이지_크기_3, 참여_인원순_정렬)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(네번째_페이지_인덱스_0_참여자_6_4일_전_마감_id_13);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(네번째_페이지_인덱스_1_참여자_6_4일_전_마감_id_9);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(네번째_페이지_인덱스_2_참여자_6_4일_전_마감_id_8);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    @Test
    void 다섯번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(4, 페이지_크기_3, 참여_인원순_정렬)
        );

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(다섯번째_페이지_인덱스_0_참여자_3_4일_전_마감_id_7);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(다섯번째_페이지_인덱스_1_참여자_2_5일_전_마감_id_1);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 페이지_크기_3_여섯번째_페이지_요청_테스트() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                검색어_맥북,
                PageRequest.of(5, 페이지_크기_3, 참여_인원순_정렬)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isEmpty();
            softAssertions.assertThat(actual.getContent()).isEmpty();
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }
}
