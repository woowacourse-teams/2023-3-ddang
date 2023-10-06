package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

/**
 * 사용하는 더미 데이터 (제목 키워드 / ID / 신뢰도 / 경매 참여자 수 / 경매 마감일 / 판매자)
 *
 * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤 / seller 1
 * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤 / seller 2
 * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤 / seller 3
 * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤 / seller 4
 * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤 / seller 5
 * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤 / seller 6
 * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤 / seller 1
 * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤 / seller 2
 * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤 / seller 2
 */
@SuppressWarnings("NonAsciiCharacters")
public class AuctionQueryByBidderIdTest extends InitializeCommonAuctionData {

    @Nested
    class 회원이_참여한_경매_목록_테스트 {

        /**
         * seller 1 기반 경매 목록
         *
         * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤
         * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
         * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
         * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
         * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
         * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
         * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
         */
        @Nested
        class Seller1_요청_테스트 {

            @Test
            void 첫번째_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3);

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                        seller1.getId(),
                        pageRequest
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction16);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction1);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction7);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 두번째_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(1, 3);

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                        seller1.getId(),
                        pageRequest
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction13);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction9);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction8);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 세번째_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(2, 3);

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                        seller1.getId(),
                        pageRequest
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(1);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction6);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }

            @Test
            void 마지막_페이지_이후_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(3, 3);

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                        seller1.getId(),
                        pageRequest
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }

        /**
         * seller 7 기반 경매 목록
         *
         * 존재하지 않음
         */
        @Nested
        class Seller7_요청_테스트 {

            @Test
            void 첫번째_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3);

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByBidderId(
                        seller7.getId(),
                        pageRequest
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }
    }
}
