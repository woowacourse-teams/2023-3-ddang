package com.ddang.ddang.bid.domain;

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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = {"id", "price"})
public class Bid extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", foreignKey = @ForeignKey(name = "fk_bid_auction"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", foreignKey = @ForeignKey(name = "fk_bid_user"))
    private User bidder;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    @Column(nullable = false)
    private BidPrice price;

    public Bid(final Auction auction, final User bidder, final BidPrice price) {
        this.auction = auction;
        this.bidder = bidder;
        this.price = price;
    }

    public boolean isSameBidder(final User bidder) {
        return this.bidder.equals(bidder);
    }

    public boolean isNextBidPriceGreaterThan(final BidPrice bidPrice) {
        return auction.isNextBidPriceGreaterThan(bidPrice);
    }
}
