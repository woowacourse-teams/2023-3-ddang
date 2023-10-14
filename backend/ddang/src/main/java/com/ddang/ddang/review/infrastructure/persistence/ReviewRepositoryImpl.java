package com.ddang.ddang.review.infrastructure.persistence;

import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JpaReviewRepository jpaReviewRepository;

    @Override
    public Review save(final Review review) {
        return jpaReviewRepository.save(review);
    }

    @Override
    public Optional<Review> findById(final Long id) {
        return jpaReviewRepository.findById(id);
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
