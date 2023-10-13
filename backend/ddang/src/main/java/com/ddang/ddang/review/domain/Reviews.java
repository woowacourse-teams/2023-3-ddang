package com.ddang.ddang.review.domain;

import com.ddang.ddang.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Getter
@ToString
public class Reviews {

    private final List<Review> reviews;

    public double addAllReviewScore() {
        return reviews.stream()
                      .mapToDouble(review -> review.getScore().getValue())
                      .sum();
    }

    public int size() {
        return reviews.size();
    }

    public boolean isEmpty() {
        return reviews.isEmpty();
    }

    public Set<User> findReviewTargets() {
        return reviews.stream()
                      .map(Review::getTarget)
                      .collect(toSet());
    }

    public List<Review> findReviewsByTarget(final User targetUser) {
        return reviews.stream()
                      .filter(review -> review.getTarget().equals(targetUser))
                      .toList();
    }

    public Long findLastReviewId() {
        final int lastIndex = reviews.size() - 1;

        return reviews.get(lastIndex)
                      .getId();
    }
}
