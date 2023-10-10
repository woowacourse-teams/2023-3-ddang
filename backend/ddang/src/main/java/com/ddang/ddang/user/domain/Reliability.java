package com.ddang.ddang.user.domain;

import com.ddang.ddang.review.domain.Review;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Reliability {

    public static final Reliability INITIAL_RELIABILITY = new Reliability(0.0d);

    private double value;

    public Reliability(final double value) {
        this.value = value;
    }

    public void updateReliability(final List<Review> reviews) {
        if (reviews.isEmpty()) {
            return;
        }

        this.value = reviews.stream()
                            .mapToDouble(review -> review.getScore().getValue())
                            .average()
                            .orElseGet(null);
    }
}
