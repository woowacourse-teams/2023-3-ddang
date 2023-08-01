package com.ddang.ddang.bid.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "price"})
public class Bid extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", foreignKey = @ForeignKey(name = "fk_bid_auction_id"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", foreignKey = @ForeignKey(name = "fk_bid_user_id"))
    private User bidder;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    @Column(nullable = false)
    private Price price;

    public Bid(final Auction auction, final User bidder, final Price price) {
        this.auction = auction;
        this.bidder = bidder;
        this.price = price;
    }

    public boolean isSameBidder(final User bidder) {
        return this.bidder.equals(bidder);
    }

    public boolean isSmallerThanBidPrice(final Price price) {
        return this.price.isMoreThan(price);
    }

    public boolean isSmallerThanNextBidPrice(final Price price) {
        return calculateNextMinimumBidPrice() > price.getValue();
    }

    private int calculateNextMinimumBidPrice() {
        return this.price.getValue() + this.auction.getBidUnit().getValue();
    }
}
