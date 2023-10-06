package com.ddang.ddang.review.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.common.entity.BaseCreateTimeEntity;
import com.ddang.ddang.user.domain.User;
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
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "content", "score"})
public class Review extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_auction"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false, foreignKey = @ForeignKey(name = "fk_review_target"))
    private User target;

    private String content;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "score"))
    private Score score;

    @Builder
    private Review(
            final Auction auction,
            final User writer,
            final User target,
            final String content,
            final Score score
    ) {
        this.auction = auction;
        this.writer = writer;
        this.target = target;
        this.content = content;
        this.score = score;
    }
}

