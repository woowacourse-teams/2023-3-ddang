package com.ddang.ddang.image.domain;

import com.ddang.ddang.image.domain.fixture.AuctionImageFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionImageTest extends AuctionImageFixture {

    @Test
    void 경매_연관_관계를_세팅한다() {
        // given
        final AuctionImage auctionImage = new AuctionImage("image.png", "image.png");

        // when
        auctionImage.initAuction(경매);

        // then
        assertThat(auctionImage.getAuction()).isNotNull();
    }
}
