package com.ddang.ddang.image.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuctionProfileImageTest {

    @Test
    void 경매_연관_관계를_세팅한다() {
        // given
        final AuctionImage auctionImage = new AuctionImage("image.png", "image.png");
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        // when
        auctionImage.initAuction(auction);

        // then
        assertThat(auctionImage.getAuction()).isNotNull();
    }
}
