package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@SuppressWarnings("NonAsciiCharacters")
class SearchTitleQueryTest extends InitializeCommonAuctionData {

    @Test
    void 검색_결과가_없을때_첫번째_페이지를_요청하면_빈_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                null,
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
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                15L,
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
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                null,
                PageRequest.of(1, 3),
                new ReadAuctionSearchCondition("핫식스")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_첫번째_페이지에는_페이지_크기만큼의_검색_결과를_가진_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                null,
                PageRequest.of(1, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction4);
            softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction3);
            softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction2);
            softAssertions.assertThat(actual.hasNext()).isTrue();
        });
    }

    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_마지막_페이지를_요청하면_검색_결과를_페이지로_나눈_나머지만큼의_검색_결과를_가진_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                auction2.getId(),
                PageRequest.of(2, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction1);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 검색_결과가_페이지_크기보다_큰_경우_마지막_페이지_이후_페이지를_요청하면_빈_Slice를_반환한다() {
        // when
        final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                auction1.getId(),
                PageRequest.of(2, 3),
                new ReadAuctionSearchCondition("맥북")
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(0);
            softAssertions.assertThat(actual.hasNext()).isFalse();
        });
    }
}
