package com.ddang.ddang.review.infrastructure.persistence;

import com.ddang.ddang.review.application.exception.ReviewNotFoundException;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.repository.ReviewRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JpaReviewRepository jpaReviewRepository;

    @Override
    public Review save(final Review review) {
        return jpaReviewRepository.save(review);
    }

    @Override
    public Review getByIdOrThrow(final Long id) {
        return jpaReviewRepository.findById(id)
                                  .orElseThrow(() -> new ReviewNotFoundException("지정한 리뷰를 찾을 수 없습니다."));
    }

    @Override
    public List<Review> findAllByTargetId(final Long targetId) {
        return jpaReviewRepository.findAllByTargetId(targetId);
    }

    @Override
    public Optional<Review> findByAuctionIdAndWriterId(final Long auctionId, final Long writerId) {
        return jpaReviewRepository.findByAuctionIdAndWriterId(auctionId, writerId);
    }

    @Override
    public List<Review> findAllByIdGreaterThan(final Long id) {
        return jpaReviewRepository.findAllByIdGreaterThan(id);
    }

    @Override
    public boolean existsByAuctionIdAndWriterId(final Long auctionId, final Long writerId) {
        return jpaReviewRepository.existsByAuctionIdAndWriterId(auctionId, writerId);
    }
}
