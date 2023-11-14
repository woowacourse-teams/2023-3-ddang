package com.ddang.ddang.auction.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.fixture.AuctionRepositoryImplFixture;
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
class AuctionRepositoryImplTest extends AuctionRepositoryImplFixture {

    @Autowired
    JpaAuctionRepository jpaAuctionRepository;

    AuctionRepository auctionRepository;

    @BeforeEach
    void setUp(@Autowired final JPAQueryFactory jpaQueryFactory) {
        auctionRepository = new AuctionRepositoryImpl(
                jpaAuctionRepository,
                new QuerydslAuctionRepository(jpaQueryFactory)
        );
    }

    @Test
    void 경매를_저장한다() {
        // when
        final Auction actual = auctionRepository.save(저장하기_전_경매_엔티티);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 지정한_id의_경매가_존재하는_경우_true를_반환한다() {
        // when
        final boolean actual = auctionRepository.existsById(저장된_경매_엔티티.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지정한_id의_경매가_존재하지_않는_경우_false를_반환한다() {
        // when
        final boolean actual = auctionRepository.existsById(존재하지_않는_경매_id);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 지정한_아이디에_대한_경매와_관련된_데이터를_모두_조회한다() {
        // when
        final Auction actual = auctionRepository.getTotalAuctionByIdOrThrow(저장된_경매_엔티티.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(저장된_경매_엔티티.getId());
            softAssertions.assertThat(actual.getTitle()).isEqualTo(저장된_경매_엔티티.getTitle());
            softAssertions.assertThat(actual.getDescription()).isEqualTo(저장된_경매_엔티티.getDescription());
            softAssertions.assertThat(actual.getBidUnit()).isEqualTo(저장된_경매_엔티티.getBidUnit());
            softAssertions.assertThat(actual.getStartPrice()).isEqualTo(저장된_경매_엔티티.getStartPrice());
            softAssertions.assertThat(actual.getClosingTime()).isEqualTo(저장된_경매_엔티티.getClosingTime());
            softAssertions.assertThat(actual.getAuctionRegions()).isNotNull();
            softAssertions.assertThat(actual.getAuctionRegions().get(0)).isNotNull();
            softAssertions.assertThat(actual.getAuctionRegions().get(0).getThirdRegion()).isNotNull();
            softAssertions.assertThat(actual.getAuctionRegions().get(0).getThirdRegion().getFirstRegion()).isNotNull();
            softAssertions.assertThat(actual.getAuctionRegions().get(0).getThirdRegion().getSecondRegion()).isNotNull();
            softAssertions.assertThat(actual.getSubCategory()).isNotNull();
            softAssertions.assertThat(actual.getSubCategory().getMainCategory()).isNotNull();
            softAssertions.assertThat(actual.getSeller()).isNotNull();
        });
    }

    @Test
    void 지정한_아이디에_대한_경매를_조회한다() {
        // when
        final Auction actual = auctionRepository.getPureAuctionByIdOrThrow(저장된_경매_엔티티.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isEqualTo(저장된_경매_엔티티.getId());
            softAssertions.assertThat(actual.getTitle()).isEqualTo(저장된_경매_엔티티.getTitle());
            softAssertions.assertThat(actual.getDescription()).isEqualTo(저장된_경매_엔티티.getDescription());
            softAssertions.assertThat(actual.getBidUnit()).isEqualTo(저장된_경매_엔티티.getBidUnit());
            softAssertions.assertThat(actual.getStartPrice()).isEqualTo(저장된_경매_엔티티.getStartPrice());
            softAssertions.assertThat(actual.getClosingTime()).isEqualTo(저장된_경매_엔티티.getClosingTime());
        });
    }

    @Test
    void 삭제된_아이디에_대한_경매_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> auctionRepository.getTotalAuctionByIdOrThrow(삭제된_경매_엔티티.getId()))
                .isInstanceOf(AuctionNotFoundException.class);
    }

    @Test
    void 경매_목록을_조회한다() {
        // when
        final Slice<Auction> actual = auctionRepository.findAuctionsAllByCondition(
                검색어_없음,
                PageRequest.of(페이지_1, 페이지_크기)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.getContent().get(0)).isEqualTo(저장된_경매_엔티티);
            assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 사용자가_등록한_경매_목록을_조회한다() {
        // when
        final Slice<Auction> actual = auctionRepository.findAuctionsAllByUserId(
                판매자.getId(),
                PageRequest.of(페이지_1, 페이지_크기)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.getContent().get(0)).isEqualTo(저장된_경매_엔티티);
            assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 사용자가_입찰한_경매_목록을_조회한다() {
        // when
        final Slice<Auction> actual = auctionRepository.findAuctionsAllByBidderId(
                구매자.getId(),
                PageRequest.of(페이지_1, 페이지_크기)
        );

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.getContent().get(0)).isEqualTo(저장된_경매_엔티티);
            assertThat(actual.hasNext()).isFalse();
        });
    }

    @Test
    void 특정_사용자가_판매자인_경매중_현재_진행_중인_경매가_있다면_참을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsBySellerIdAndAuctionStatusIsOngoing(
                판매자.getId(),
                저장된_경매_엔티티.getClosingTime().minusDays(1)
        );

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_사용자가_판매자인_경매중_현재_진행_중인_경매가_없다면_거짓을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsBySellerIdAndAuctionStatusIsOngoing(
                판매자.getId(),
                저장된_경매_엔티티.getClosingTime().plusDays(1)
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 특정_사용자가_판매자인_경매중_현재_진행_중인_경매가_있지만_해당_경매가_삭제_됐다면_거짓을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsBySellerIdAndAuctionStatusIsOngoing(
                삭제한_경매를_갖고_있는_판매자.getId(),
                삭제된_경매만_있는_사용자의_삭제된_경매_엔티티.getClosingTime().minusDays(1)
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 특정_사용자가_마지막_입찰자인_경매중_현재_진행_중인_경매가_있다면_참을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsLastBidByUserIdAndAuctionStatusIsOngoing(
                구매자.getId(),
                저장된_경매_엔티티.getClosingTime().minusDays(1)
        );

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 특정_사용자가_마지막_입찰자인_경매중_현재_진행_중인_경매가_없다면_거짓을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsLastBidByUserIdAndAuctionStatusIsOngoing(
                구매자.getId(),
                저장된_경매_엔티티.getClosingTime().plusDays(1)
        );

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 특정_사용자가_마지막_입찰자인_경매중_현재_진행_중인_경매가_없지만_삭제_됐다면_거짓을_반환한다() {
        // when
        final boolean actual = auctionRepository.existsLastBidByUserIdAndAuctionStatusIsOngoing(
                삭제된_경매의_마지막_입찰자.getId(),
                삭제된_경매_엔티티.getClosingTime().minusDays(1)
        );

        // then
        assertThat(actual).isFalse();
    }
}
