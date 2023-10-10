package com.ddang.ddang.review.domain;

import com.ddang.ddang.review.domain.exception.InvalidScoreException;
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
public class Score {

    private static final double SCORE_UNIT = 0.5;

    private double value;

    public Score(final double value) {
        if (value % SCORE_UNIT != 0) {
            throw new InvalidScoreException("평가 점수는 0.5 단위여야 합니다.");
        }

        this.value = value;
    }
}
