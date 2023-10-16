package com.ddang.ddang.review.domain.repository;

import com.ddang.ddang.review.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    Review save(final Review review);

    Optional<Review> findById(final Long id);

    List<Review> findAllByTargetId(final Long targetId);

    Optional<Review> findByAuctionIdAndWriterId(final Long auctionId, final Long writerId);

    List<Review> findAllByIdGreaterThan(final Long id);

    boolean existsByAuctionIdAndWriterId(final Long auctionId, final Long writerId);
}
