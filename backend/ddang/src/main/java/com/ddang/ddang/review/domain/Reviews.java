package com.ddang.ddang.review.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
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
}
