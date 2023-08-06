package com.ddang.ddang.auction.domain;

import com.ddang.ddang.auction.domain.exception.InvalidPriceValueException;
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
public class BidUnit {

    private static final int MINIMUM_BID_UNIT = 0;
    private static final int MAXIMUM_BID_UNIT = 2_100_000_000;
    private static final int AVAILABLE_BID_UNIT = 100;

    private int value;

    public BidUnit(final int value) {
        if (value < MINIMUM_BID_UNIT) {
            throw new InvalidPriceValueException(String.format("입찰 단위는 %d원 이상이어야 합니다.", MINIMUM_BID_UNIT));
        }
        if (value > MAXIMUM_BID_UNIT) {
            throw new InvalidPriceValueException(String.format("입찰 단위는 %d원 이하여야 합니다.", MAXIMUM_BID_UNIT));
        }
        if (value % AVAILABLE_BID_UNIT != 0) {
            throw new InvalidPriceValueException(String.format("입찰 단위는 %d의 배수여야 합니다.", AVAILABLE_BID_UNIT));
        }

        this.value = value;
    }
}
