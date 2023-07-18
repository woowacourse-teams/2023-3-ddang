package com.ddang.ddang.actuion.domain;

import com.ddang.ddang.actuion.domain.exception.InvalidPriceValueException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Price {

    private static final int MINIMUM_PRICE = 0;
    private static final int MAXIMUM_PRICE = 2100000000;

    private int value;

    public Price(final int value) {
        if (value < MINIMUM_PRICE) {
            throw new InvalidPriceValueException(String.format("가격은 %d원 이상이어야 합니다.", MINIMUM_PRICE));
        }
        if (value > MAXIMUM_PRICE) {
            throw new InvalidPriceValueException(String.format("가격은 %d원 이하여야 합니다.", MAXIMUM_PRICE));
        }

        this.value = value;
    }
}
