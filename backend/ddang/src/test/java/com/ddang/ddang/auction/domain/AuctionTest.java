package com.ddang.ddang.auction.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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

    @Test
    void 경매에_직거래_지역_정보를_추가한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("강남구");
        final Region thirdRegion = new Region("역삼동");

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);

        final AuctionRegion auctionRegion = new AuctionRegion(firstRegion);

        // when
        auction.addAuctionRegions(List.of(auctionRegion));

        // then
        assertThat(auction.getAuctionRegions()).hasSize(1);
    }

    @Test
    void 경매_이미지_연관_관계를_세팅한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();
        final AuctionImage auctionImage = new AuctionImage("image.png", "image.png");

        // when
        auction.addAuctionImage(auctionImage);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(auction.getAuctionImages()).isNotEmpty();
            softAssertions.assertThat(auctionImage.getAuction()).isNotNull();
        });
    }
}
