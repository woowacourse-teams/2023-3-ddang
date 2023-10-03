package com.ddang.ddang.review.infrastructure.persistence;

import com.ddang.ddang.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByAuctionIdAndWriterId(final Long auctionId, final Long writerId);

    @Query("""
            SELECT r FROM Review r JOIN FETCH r.writer w JOIN FETCH r.target t 
            WHERE t.id = :targetId 
            ORDER BY r.id DESC
           """)
    List<Review> findAllByTargetId(final Long targetId);

    Optional<Review> findByAuctionIdAndWriterId(final Long auctionId, final Long writerId);
}
