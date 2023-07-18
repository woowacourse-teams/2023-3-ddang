package com.ddang.ddang.auction.domain;

import com.ddang.ddang.common.entity.BaseTimeEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString
public class Auction extends BaseTimeEntity {

    private static final boolean DELETED_STATUS = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;

    @Lob
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bid_unit"))
    private BidUnit bidUnit;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "start_bid_price"))
    private Price startBidPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "last_bid_price"))
    private Price lastBidPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "winning_bid_price"))
    private Price winningBidPrice;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    private LocalDateTime closingTime;

    @Builder
    private Auction(
            final String title,
            final String description,
            final BidUnit bidUnit,
            final Price startBidPrice,
            final LocalDateTime closingTime
    ) {
        this.title = title;
        this.description = description;
        this.bidUnit = bidUnit;
        this.startBidPrice = startBidPrice;
        this.closingTime = closingTime;
    }

    public void delete() {
        deleted = DELETED_STATUS;
    }
}
