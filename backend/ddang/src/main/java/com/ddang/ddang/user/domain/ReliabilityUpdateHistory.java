package com.ddang.ddang.user.domain;

import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", })
public class ReliabilityUpdateHistory extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lastAppliedReviewId = 0L;

    public ReliabilityUpdateHistory(final Long lastAppliedReviewId) {
        this.lastAppliedReviewId = lastAppliedReviewId;
    }
}
