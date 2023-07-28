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
@ToString(exclude = {"auction", "bidder"})
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

    public boolean isLastBidder(final User user) {
        return bidder.equals(user);
    }

    // TODO: 2023/07/28 (고민) 메서드 명에 Price를 넣는게 어색해서 고민이네요, 없애면 Bid를 기준으로 이해하기가 어려울 것 같고요...!
    public boolean isPriceMoreThan(final Price otherPrice) {
        return this.price.isMoreThan(otherPrice);
    }

    // TODO: 2023/07/28 필드 관련 메서드 외 메서드 명에 getter 괜찮은가?
    public int getNextMinimumBidPrice() {
        return this.price.getValue() + this.auction.getBidUnit().getValue();
    }
}
