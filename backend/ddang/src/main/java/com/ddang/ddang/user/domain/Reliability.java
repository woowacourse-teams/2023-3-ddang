package com.ddang.ddang.user.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Reliability {

    private static final double INITIAL_RELIABILITY_VALUE = Double.MIN_VALUE;
    public static final Reliability INITIAL_RELIABILITY = new Reliability(INITIAL_RELIABILITY_VALUE);

    private double value;

    public Reliability(final double value) {
        this.value = value;
    }

    public double calculateReviewScoreSum(final int appliedReviewCount) {
        return value * appliedReviewCount;
    }
}
