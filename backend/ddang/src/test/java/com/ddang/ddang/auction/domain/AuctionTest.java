package com.ddang.ddang.auction.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionTest {

    @Test
    void 경매를_삭제한다() {
        // given
        final Auction auction = Auction.builder()
                               .title("title")
                               .build();

        // when
        auction.delete();

        // then
        assertThat(auction.isDeleted()).isTrue();
    }
}
