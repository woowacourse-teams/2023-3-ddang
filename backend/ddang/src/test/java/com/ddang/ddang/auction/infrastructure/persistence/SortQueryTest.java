package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import java.time.LocalDateTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
 */
@SuppressWarnings("NonAsciiCharacters")
class SortQueryTest extends InitializeCommonAuctionData {

    /**
     * id 기준 정렬 순서
     *
     * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
     * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
     * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
     * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤
     * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
     * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
     * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
     * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
     */
    @Nested
    class Id_기준_정렬_조회_테스트 {

        @Test
        void 첫번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortById(null)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction8);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction7);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction6);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 두번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortById(auction6.getId())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction4);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction3);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 세번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortById(auction3.getId())
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
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortById(auction1.getId())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(0);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        private ReadAuctionCondition createReadAuctionConditionSortById(final Long lastAuctionId) {
            return new ReadAuctionCondition("id", lastAuctionId, null, null, null, null, 3);
        }
    }

    /**
     * 신뢰도 기준 정렬 순서
     *
     * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
     * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
     * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
     * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
     * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
     * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
     * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤
     * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
     */
    @Nested
    class 신뢰도_정렬_조회_테스트 {

        @Test
        void 첫번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByReliability(null)
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
        void 두번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByReliability(auction1.getSeller().getReliability())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction8);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction2);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction3);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 세번째_페이지_요청() {
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByReliability(auction3.getSeller().getReliability())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction6);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        @Test
        void 마지막_페이지_이후_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByReliability(auction6.getSeller().getReliability())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(0);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        private ReadAuctionCondition createReadAuctionConditionSortByReliability(final Double lastReliability) {
            return new ReadAuctionCondition("reliability", null, null, null, lastReliability, null, 3);
        }
    }

    /**
     * 경매 참여자 수 기준 정렬 순서
     *
     * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
     * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
     * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
     * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤
     * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
     * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
     * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
     * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
     */
    @Nested
    class 경매_참여자_수_정렬_조회_테스트 {

        @Test
        void 첫번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByAuctioneerCount(null)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction6);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction4);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction8);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 두번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByAuctioneerCount(auction8.getAuctioneerCount())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction3);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction7);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 세번째_페이지_요청() {
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByAuctioneerCount(auction7.getAuctioneerCount())
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
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByAuctioneerCount(auction2.getAuctioneerCount())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(0);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        private ReadAuctionCondition createReadAuctionConditionSortByAuctioneerCount(final Integer lastAuctioneerCount) {
            return new ReadAuctionCondition("auctioneerCount", null, lastAuctioneerCount, null, null, null, 3);
        }
    }

    /**
     * 마감 임박순 기준 정렬 순서
     *
     * Auction5 : 핫식스 / 5L / 1.5d / 4 / 1일 뒤
     * Auction4 : 맥북 / 4L / 5.0d / 7 / 2일 뒤
     * Auction6 : 레드불 / 6L / 0.3d / 8 / 2일 뒤
     * Auction3 : 맥북 / 3L / 2.1d / 4 / 3일 뒤
     * Auction7 : 맥북 / 7L / 4.7d / 3 / 3일 뒤
     * Auction2 : 맥북 / 2L / 3.5d / 1 / 4일 뒤
     * Auction8 : 맥북 / 8L / 3.5d / 6 / 4일 뒤
     * Auction1 : 맥북 / 1L / 4.7d / 2 / 5일 뒤
     */
    @Nested
    class 마감_임박순_정렬_조회_테스트 {

        @Test
        void 첫번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByClosingTime(null)
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction5);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction4);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction6);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 두번째_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByClosingTime(auction6.getClosingTime())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction3);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction7);
                softAssertions.assertThat(actual.getContent().get(2)).isEqualTo(auction2);
                softAssertions.assertThat(actual.hasNext()).isTrue();
            });
        }

        @Test
        void 세번째_페이지_요청() {
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByClosingTime(auction2.getClosingTime())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(2);
                softAssertions.assertThat(actual.getContent().get(0)).isEqualTo(auction8);
                softAssertions.assertThat(actual.getContent().get(1)).isEqualTo(auction1);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        @Test
        void 마지막_페이지_이후_페이지_요청() {
            // when
            final Slice<Auction> actual = querydslAuctionRepository.findAuctionsAllByLastAuctionId(
                    createReadAuctionConditionSortByClosingTime(auction1.getClosingTime())
            );

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(0);
                softAssertions.assertThat(actual.hasNext()).isFalse();
            });
        }

        private ReadAuctionCondition createReadAuctionConditionSortByClosingTime(final LocalDateTime lastClosingTime) {
            return new ReadAuctionCondition("closingTime", null, null, lastClosingTime, null, null, 3);
        }
    }
}
