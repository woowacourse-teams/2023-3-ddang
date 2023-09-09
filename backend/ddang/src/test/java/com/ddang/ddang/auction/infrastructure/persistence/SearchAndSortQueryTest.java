package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

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
 */
@SuppressWarnings("NonAsciiCharacters")
public class SearchAndSortQueryTest extends InitializeCommonAuctionData {

    @Nested
    class 맥북_검색_테스트 {

        final ReadAuctionSearchCondition searchCondition = new ReadAuctionSearchCondition("맥북");

        /**
         * id 기준 정렬 순서
         *
         * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤
         * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
         * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
         * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
         * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤
         * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤
         * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤
         * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
         * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
         * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
         * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
         * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
         * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
         * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
         */
        @Nested
        class Id_기준_정렬_조회_테스트 {

            @Test
            void 첫번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Order.desc("id")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
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

            @Test
            void 두번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Order.desc("id")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction13);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction12);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction11);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 세번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(2, 3, Sort.by(Order.desc("id")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction10);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction9);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction8);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 네번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(3, 3, Sort.by(Order.desc("id")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction7);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction4);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction3);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 다섯번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(4, 3, Sort.by(Order.desc("id")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
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
            void 마지막_페이지_이후_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(5, 3, Sort.by(Order.desc("reliability")));


                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }

        /**
         * 경매 참여자 수 기준 정렬 순서
         *
         * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
         * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤
         * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
         * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
         * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
         * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤
         * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤
         * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤
         * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
         * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
         * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
         * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
         * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
         * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
         */
        @Nested
        class 경매_참여자_수_정렬_조회_테스트 {

            @Test
            void 첫번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Order.desc("auctioneerCount")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction4);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction16);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction15);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 두번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Order.desc("auctioneerCount")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction14);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction13);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction12);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 세번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(2, 3, Sort.by(Order.desc("auctioneerCount")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction11);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction10);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction9);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 네번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(3, 3, Sort.by(Order.desc("auctioneerCount")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction8);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction3);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction7);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 다섯번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(4, 3, Sort.by(Order.desc("auctioneerCount")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(2);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction1);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction2);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }

            @Test
            void 마지막_페이지_이후_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(5, 3, Sort.by(Order.desc("reliability")));


                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }

        /**
         * 신뢰도 기준 정렬 순서
         *
         * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
         * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
         * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
         * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤
         * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
         * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
         * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
         * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤
         * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤
         * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤
         * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
         * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
         * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
         * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
         *
         */
        @Nested
        class 신뢰도_정렬_조회_테스트 {

            @Test
            void 첫번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Order.desc("reliability")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction4);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction7);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction1);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 두번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Order.desc("reliability")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
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

            @Test
            void 세번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(2, 3, Sort.by(Order.desc("reliability")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction13);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction12);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction11);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 네번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(3, 3, Sort.by(Order.desc("reliability")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction10);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction9);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction8);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 다섯번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(4, 3, Sort.by(Order.desc("reliability")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(2);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction2);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction3);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }

            @Test
            void 마지막_페이지_이후_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(5, 3, Sort.by(Order.desc("reliability")));


                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }

        /**
         * 마감 임박순 기준 정렬 순서
         *
         * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
         * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
         * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
         * Auction16 : 맥북 / 16L / 3.5d / 6 / 4일 뒤
         * Auction15 : 맥북 / 15L / 3.5d / 6 / 4일 뒤
         * Auction14 : 맥북 / 14L / 3.5d / 6 / 4일 뒤
         * Auction13 : 맥북 / 13L / 3.5d / 6 / 4일 뒤
         * Auction12 : 맥북 / 12L / 3.5d / 6 / 4일 뒤
         * Auction11 : 맥북 / 11L / 3.5d / 6 / 4일 뒤
         * Auction10 : 맥북 / 10L / 3.5d / 6 / 4일 뒤
         * Auction9 : 맥북 / 9L / 3.5d / 6 / 4일 뒤
         * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
         * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
         * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
         */
        @Nested
        class 마감_임박순_정렬_조회_테스트 {

            @Test
            void 첫번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Order.asc("closingTime")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction4);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction7);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction3);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 두번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Order.asc("closingTime")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
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

            @Test
            void 세번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(2, 3, Sort.by(Order.asc("closingTime")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction13);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction12);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction11);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 네번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(3, 3, Sort.by(Order.asc("closingTime")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
                );

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(3);
                    softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction10);
                    softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction9);
                    softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction8);
                    softAssertions.assertThat(actual.hasNext()).isTrue();
                });
            }

            @Test
            void 다섯번째_페이지_테스트() {
                // given
                final PageRequest pageRequest = PageRequest.of(4, 3, Sort.by(Order.asc("closingTime")));

                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition
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
            void 마지막_페이지_이후_페이지_요청() {
                // given
                final PageRequest pageRequest = PageRequest.of(5, 3, Sort.by(Order.asc("closingTime")));


                // when
                final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByCondition(
                        pageRequest,
                        searchCondition);

                // then
                SoftAssertions.assertSoftly(softAssertions -> {
                    softAssertions.assertThat(actual).hasSize(0);
                    softAssertions.assertThat(actual.hasNext()).isFalse();
                });
            }
        }
    }
}
