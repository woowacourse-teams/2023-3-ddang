package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

/**
 * 사용하는 더미 데이터 (제목 키워드 / ID / 신뢰도 / 경매 참여자 수 / 경매 마감일)
 *
 * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
 * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
 * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
 * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
 * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤
 * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
 * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
 * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
 * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
 * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤
 * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤
 * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤
 * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
 * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
 * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
 * Auction16 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
 * Auction17 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
 */
@SuppressWarnings("NonAsciiCharacters")
class AuctionSearchTitleQueryTest extends InitializeCommonAuctionData {

    @Test
    void 검색_결과가_없을때_첫번째_페이지를_요청하면_빈_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(1, 3),
                new ReadAuctionSearchCondition("우아한테크코스")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(0);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 검색_결과가_없을때_두번째_페이지_이후의_페이지를_요청하면_빈_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(1, 3),
                new ReadAuctionSearchCondition("우아한테크코스")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(0);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 검색_결과가_페이지_크기보다_작은_경우_첫번째_페이지에_모든_검색_결과를_가진_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(0, 3),
                new ReadAuctionSearchCondition("핫식스")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    /**
     * 제목 "맥북" 검색 시 결과
     *
     * Auction16 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
     * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
     * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
     */
    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_첫번째_페이지에는_페이지_크기만큼의_검색_결과를_가진_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(0, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction16);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction15);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction14);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    /**
     * 제목 "맥북" 검색 시 결과
     *
     * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
     * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
     */
    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_마지막_페이지를_요청하면_검색_결과를_페이지로_나눈_나머지만큼의_검색_결과를_가진_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(4, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction2);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction1);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_마지막_페이지_이후_페이지를_요청하면_빈_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                PageRequest.of(5, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(0);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }
}
