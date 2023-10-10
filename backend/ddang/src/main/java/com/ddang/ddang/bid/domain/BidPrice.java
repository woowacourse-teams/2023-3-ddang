package com.ddang.ddang.bid.domain;

import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class BidPrice {

    private static final int MINIMUM_PRICE = 0;
    private static final int MAXIMUM_PRICE = 2_100_000_000;

    private int value;

    public BidPrice(final int value) {
        if (value < MINIMUM_PRICE) {
            throw new InvalidBidPriceException(String.format("가격은 %d원 이상이어야 합니다.", MINIMUM_PRICE));
        }
        if (value > MAXIMUM_PRICE) {
            throw new InvalidBidPriceException(String.format("가격은 %d원 이하여야 합니다.", MAXIMUM_PRICE));
        }

        this.value = value;
    }

    public boolean isGreaterThan(final BidPrice otherBidPrice) {
        return this.value > otherBidPrice.value;
    }
}
