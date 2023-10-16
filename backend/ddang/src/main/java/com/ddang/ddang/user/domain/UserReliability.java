package com.ddang.ddang.user.domain;

import com.ddang.ddang.review.domain.Reviews;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "reliability", "appliedReviewCount"})
public class UserReliability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_reliability_user"), nullable = false)
    private User user;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reliability"))
    private Reliability reliability;

    private int appliedReviewCount = 0;

    public UserReliability(final User user) {
        this.user = user;
        this.reliability = user.getReliability();
    }

    public void updateReliability(final Reviews newReviews) {
        if (newReviews.isEmpty()) {
            return;
        }
        final Reliability newReliability = calculateNewReliability(newReviews);

        this.reliability = newReliability;
        addAppliedReviewCount(newReviews.size());
        user.updateReliability(newReliability);
    }

    private Reliability calculateNewReliability(final Reviews newReviews) {
        final double currentReviewScoreSum = reliability.calculateReviewScoreSum(appliedReviewCount);
        final double newReviewScoreSum = newReviews.addAllReviewScore();
        final int allReviewCount = appliedReviewCount + newReviews.size();

        final double newReliabilityValue = (currentReviewScoreSum + newReviewScoreSum) / allReviewCount;

        return new Reliability(newReliabilityValue);
    }

    private void addAppliedReviewCount(final int newReviewCount) {
        this.appliedReviewCount += newReviewCount;
    }
}
