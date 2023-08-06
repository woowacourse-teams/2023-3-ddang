package com.ddang.ddang.region.domain;

import com.ddang.ddang.auction.domain.Auction;
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
@ToString(exclude = {"auction", "thirdRegion"})
public class AuctionRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", foreignKey = @ForeignKey(name = "fk_auction_region_auction"))
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_region_id", foreignKey = @ForeignKey(name = "fk_auction_region_third_region"))
    private Region thirdRegion;

    public AuctionRegion(final Region thirdRegion) {
        this.thirdRegion = thirdRegion;
    }

    public void initAuction(final Auction auction) {
        this.auction = auction;
    }
}
