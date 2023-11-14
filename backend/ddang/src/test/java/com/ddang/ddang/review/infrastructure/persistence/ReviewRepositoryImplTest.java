package com.ddang.ddang.review.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.review.application.exception.ReviewNotFoundException;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.repository.ReviewRepository;
import com.ddang.ddang.review.infrastructure.persistence.fixture.ReviewRepositoryImplFixture;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReviewRepositoryImplTest extends ReviewRepositoryImplFixture {

    ReviewRepository reviewRepository;

    @BeforeEach
    void setUp(@Autowired final JpaReviewRepository jpaReviewRepository) {
        reviewRepository = new ReviewRepositoryImpl(jpaReviewRepository);
    }

    @Test
    void 리뷰를_저장한다() {
        // when
        reviewRepository.save(저장하려는_리뷰);

        // then
        assertThat(저장하려는_리뷰.getId()).isPositive();
    }

    @Test
    void 지정한_경매_아이디와_작성자_아이디를_포함하는_리뷰가_존재하면_참을_반환한다() {
        // when
        final boolean actual = reviewRepository.existsByAuctionIdAndWriterId(판매자1이_리뷰한_경매.getId(), 판매자1.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 지정한_채팅방_아이디를_포함하는_리뷰가_존재하지_않는다면_거짓을_반환한다() {
        // when
        final boolean actual = reviewRepository.existsByAuctionIdAndWriterId(리뷰_안한_경매.getId(), 리뷰_안한_경매_판매자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 지정한_아이디가_리뷰_대상_아이디에_해당하는_리뷰_목록을_최신순으로_조회한다() {
        // when
        final List<Review> actual = reviewRepository.findAllByTargetId(구매자.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(구매자가_판매자2에게_받은_리뷰);
            softAssertions.assertThat(actual.get(1)).isEqualTo(구매자가_판매자1에게_받은_리뷰);
        });
    }

    @Test
    void 지정한_경매_아이디와_작성자_아이디가_해당하는_리뷰가_존재한다면_optional에_넣어_반환한다() {
        // when
        final Optional<Review> actual =
                reviewRepository.findByAuctionIdAndWriterId(판매자1이_리뷰한_경매.getId(), 판매자1.getId());

        // then
        assertThat(actual).contains(구매자가_판매자1에게_받은_리뷰);
    }

    @Test
    void 지정한_경매_아이디와_작성자_아이디가_해당하는_리뷰가_존재하지_않는다면_빈_optional을_반환한다() {
        // when
        final Optional<Review> actual =
                reviewRepository.findByAuctionIdAndWriterId(리뷰_안한_경매.getId(), 리뷰_안한_경매_판매자.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 지정한_리뷰_아이디보다_아이디가_큰_리뷰_목록을_조회한다() {
        // when
        final List<Review> actual = reviewRepository.findAllByIdGreaterThan(구매자가_판매자1에게_받은_리뷰.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);
            softAssertions.assertThat(actual.get(0)).isEqualTo(구매자가_판매자2에게_받은_리뷰);
        });
    }

    @Test
    void 지정한_리뷰를_조회한다() {
        // when
        final Review actual = reviewRepository.getByIdOrThrow(구매자가_판매자1에게_받은_리뷰.getId());

        // then
        assertThat(actual).isEqualTo(구매자가_판매자1에게_받은_리뷰);
    }

    @Test
    void 지정한_리뷰가_없을_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reviewRepository.getByIdOrThrow(존재하지_않는_리뷰_id))
                .isInstanceOf(ReviewNotFoundException.class);
    }
}
