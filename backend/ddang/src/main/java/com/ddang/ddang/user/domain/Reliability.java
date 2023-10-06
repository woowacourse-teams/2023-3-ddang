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

    public static final Reliability INITIAL_RELIABILITY = new Reliability(null);

    private Float value;

    public Reliability(final Float value) {
        this.value = value;
    }

    public void updateReliability(final List<Review> reviews) {
        if (reviews.isEmpty()) {
            this.value = null;

            return;
        }

        Float scoreSum = 0.0f;
        for (final Review review : reviews) {
            scoreSum += review.getScore().getValue();
        }

        this.value = scoreSum / reviews.size();
    }
}
