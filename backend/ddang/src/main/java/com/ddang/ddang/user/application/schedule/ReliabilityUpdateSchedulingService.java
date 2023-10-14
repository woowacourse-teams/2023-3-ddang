package com.ddang.ddang.user.application.schedule;

import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Reviews;
import com.ddang.ddang.review.infrastructure.persistence.JpaReviewRepository;
import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.domain.repository.ReliabilityUpdateHistoryRepository;
import com.ddang.ddang.user.domain.repository.UserReliabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReliabilityUpdateSchedulingService {

    private final ReliabilityUpdateHistoryRepository updateHistoryRepository;
    private final JpaReviewRepository reviewRepository;
    private final UserReliabilityRepository userReliabilityRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 ? * MON")
    public void updateAllUserReliability() {
        final ReliabilityUpdateHistory updateHistory = updateHistoryRepository.findFirstByOrderByIdDesc()
                                                                              .orElse(new ReliabilityUpdateHistory());
        final Long lastAppliedReviewId = updateHistory.getLastAppliedReviewId();
        final List<Review> findNewReviews = reviewRepository.findAllByIdGreaterThan(lastAppliedReviewId);
        final Reviews newReviews = new Reviews(findNewReviews);

        if (newReviews.isEmpty()) {
            return;
        }

        update(newReviews);
    }

    private void update(final Reviews newReviews) {
        final Set<User> targetUsers = newReviews.findReviewTargets();
        for (final User targetUser : targetUsers) {
            final UserReliability userReliability = userReliabilityRepository.findByUserId(targetUser.getId())
                                                                             .orElseGet(() -> createUserReliability(targetUser));

            final List<Review> targetReviews = newReviews.findReviewsByTarget(targetUser);

            userReliability.updateReliability(new Reviews(targetReviews));
        }

        final ReliabilityUpdateHistory newHistory = new ReliabilityUpdateHistory(newReviews.findLastReviewId());

        updateHistoryRepository.save(newHistory);
    }

    private UserReliability createUserReliability(final User user) {
        final UserReliability userReliability = new UserReliability(user);

        return userReliabilityRepository.save(userReliability);
    }
}
