package com.ddang.ddang.auction.domain;

import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.common.entity.BaseTimeEntity;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.user.domain.User;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString(of = {"id", "title", "description", "bidUnit", "startPrice", "deleted", "closingTime"})
public class Auction extends BaseTimeEntity {

    private static final boolean DELETED_STATUS = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", foreignKey = @ForeignKey(name = "fk_auction_seller"))
    private User seller;

    @Column(length = 30)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bid_unit"))
    private BidUnit bidUnit;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "start_price"))
    private Price startPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_bid_id", foreignKey = @ForeignKey(name = "fk_auction_last_bid"))
    private Bid lastBid;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    private LocalDateTime closingTime;

    @OneToMany(mappedBy = "auction", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AuctionRegion> auctionRegions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", foreignKey = @ForeignKey(name = "fk_auction_sub_category"))
    private Category subCategory;

    @OneToMany(mappedBy = "auction", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AuctionImage> auctionImages = new ArrayList<>();

    private int auctioneerCount = 0;

    @Builder
    private Auction(
            final String title,
            final String description,
            final BidUnit bidUnit,
            final Price startPrice,
            final LocalDateTime closingTime,
            final User seller,
            final Category subCategory
    ) {
        this.title = title;
        this.description = description;
        this.bidUnit = bidUnit;
        this.startPrice = startPrice;
        this.closingTime = closingTime;
        this.seller = seller;
        this.subCategory = subCategory;
    }

    public void delete() {
        deleted = DELETED_STATUS;
    }

    public void addAuctionRegions(final List<AuctionRegion> auctionRegions) {
        for (final AuctionRegion auctionRegion : auctionRegions) {
            this.auctionRegions.add(auctionRegion);
            auctionRegion.initAuction(this);
        }
    }

    public void addAuctionImages(final List<AuctionImage> auctionImages) {
        for (final AuctionImage auctionImage : auctionImages) {
            this.auctionImages.add(auctionImage);
            auctionImage.initAuction(this);
        }
    }

    public AuctionStatus findAuctionStatus(final LocalDateTime targetTime) {
        if (targetTime.isBefore(closingTime) && lastBid == null) {
            return AuctionStatus.UNBIDDEN;
        }
        if (targetTime.isBefore(closingTime) && lastBid != null) {
            return AuctionStatus.ONGOING;
        }
        if (targetTime.isAfter(closingTime) && lastBid == null) {
            return AuctionStatus.FAILURE ;
        }
        return AuctionStatus.SUCCESS;
    }

    public boolean isOwner(final User user) {
        return this.seller.equals(user);
    }

    public boolean isClosed(final LocalDateTime targetTime) {
        return targetTime.isAfter(closingTime);
    }

    public boolean isInvalidFirstBidPrice(final BidPrice bidPrice) {
        final BidPrice startBidPrice = new BidPrice(startPrice.getValue());

        return startBidPrice.isGreaterThan(bidPrice);
    }

    public void updateLastBid(final Bid lastBid) {
        this.lastBid = lastBid;
        this.auctioneerCount += 1;
    }

    public boolean isNextBidPriceGreaterThan(final BidPrice bidPrice) {
        return calculateNextMinimumBidPrice().isGreaterThan(bidPrice);
    }

    private BidPrice calculateNextMinimumBidPrice() {
        final int nextMinimumBidPrice = this.lastBid.getPrice().getValue() + this.bidUnit.getValue();
        return new BidPrice(nextMinimumBidPrice);
    }

    public boolean isSellerOrWinner(final User user, final LocalDateTime targetTime) {
        return isOwner(user) || isWinner(user, targetTime);
    }

    public boolean isWinner(final User user, final LocalDateTime targetTime) {
        return findWinner(targetTime).filter(user::equals)
                                     .isPresent();
    }

    public Optional<User> findWinner(final LocalDateTime targetTime) {
        if (isWinnerExist(targetTime)) {
            return Optional.of(lastBid.getBidder());
        }

        return Optional.empty();
    }

    private boolean isWinnerExist(final LocalDateTime targetTime) {
        return auctioneerCount != 0 && isClosed(targetTime);
    }

    public Optional<User> findLastBidder() {
        if (lastBid == null) {
            return Optional.empty();
        }

        return Optional.of(lastBid.getBidder());
    }
}
