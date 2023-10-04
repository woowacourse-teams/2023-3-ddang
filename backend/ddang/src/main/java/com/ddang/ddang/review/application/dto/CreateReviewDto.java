package com.ddang.ddang.review.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.review.presentation.dto.request.CreateReviewRequest;
import com.ddang.ddang.user.domain.User;

public record CreateReviewDto(Long auctionId, Long writerId, Long targetId, String content, Double score) {

    public static CreateReviewDto of(final Long writerId, final CreateReviewRequest createReviewRequest) {
        return new CreateReviewDto(
                createReviewRequest.auctionId(),
                writerId,
                createReviewRequest.targetId(),
                createReviewRequest.content(),
                createReviewRequest.score()
        );
    }

    public Review toEntity(final Auction auction, final User writer, final User target) {
        return Review.builder()
                     .auction(auction)
                     .writer(writer)
                     .target(target)
                     .content(content)
                     .score(new Score(score))
                     .build();
    }
}
