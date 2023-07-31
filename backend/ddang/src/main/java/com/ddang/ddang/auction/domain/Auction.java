package com.ddang.ddang.auction.domain;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.common.entity.BaseTimeEntity;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.region.domain.AuctionRegion;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "text")
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "bid_unit"))
    private BidUnit bidUnit;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "start_price"))
    private Price startPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "last_bid_price"))
    private Price lastBidPrice;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "winning_bid_price"))
    private Price winningBidPrice;

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

    @Builder
    private Auction(
            final String title,
            final String description,
            final BidUnit bidUnit,
            final Price startPrice,
            final LocalDateTime closingTime,
            final Category subCategory
    ) {
        this.title = title;
        this.description = description;
        this.bidUnit = bidUnit;
        this.startPrice = startPrice;
        this.closingTime = closingTime;
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
}
