package com.ddang.ddang.review.application;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.review.application.dto.CreateReviewDto;
import com.ddang.ddang.review.application.dto.ReadReviewDto;
import com.ddang.ddang.review.application.exception.AlreadyReviewException;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.infrastructure.persistence.JpaReviewRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final JpaReviewRepository reviewRepository;
    private final JpaAuctionRepository auctionRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public Long create(final CreateReviewDto reviewDto) {
        final Auction findAuction = auctionRepository.findById(reviewDto.auctionId())
                                                     .orElseThrow(() ->
                                                             new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.")
                                                     );
        final User writer = userRepository.findById(reviewDto.writerId())
                                          .orElseThrow(() -> new UserNotFoundException("작성자 정보를 찾을 수 없습니다."));
        final User target = userRepository.findById(reviewDto.targetId())
                                          .orElseThrow(() -> new UserNotFoundException("평가 상대의 정보를 찾을 수 없습니다."));

        if (reviewRepository.existsByAuctionIdAndWriterId(findAuction.getId(), writer.getId())) {
            throw new AlreadyReviewException("이미 평가하였습니다.");
        }

        final Review review = reviewDto.toEntity(findAuction, writer, target);
        final Review persistReview = reviewRepository.save(review);

        final List<Review> targetReviews = reviewRepository.findAllByTargetId(target.getId());
        target.updateReliability(targetReviews);

        return persistReview.getId();
    }

    public List<ReadReviewDto> readAllByTargetId(final Long targetId) {
        final List<Review> targetReviews = reviewRepository.findAllByTargetId(targetId);

        return targetReviews.stream()
                            .map(ReadReviewDto::from)
                            .toList();
    }
}
