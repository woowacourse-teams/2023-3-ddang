package com.ddang.ddang.auction.domain.fixture;

import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionFixture {

    protected AuctionRegion 서울특별시_강남구_역삼동;
    protected BidPrice 경매_시작가보다_적은_입찰_금액 = new BidPrice(900);
    protected BidPrice 유효한_입찰_금액 = new BidPrice(10_000);
    protected BidPrice 마지막_입찰가보다_적은_입찰_금액 = new BidPrice(5_000);
    protected AuctionImage 경매_이미지;
    protected User 판매자 = User.builder()
                                 .name("판매자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(new Reliability(4.7f))
                                 .oauthId("12345")
                                 .build();
    protected User 구매자 = User.builder()
                                 .name("구매자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(new Reliability(4.7f))
                                 .oauthId("54321")
                                 .build();
    protected User 사용자 = User.builder()
                             .name("사용자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(new Reliability(4.7f))
                             .oauthId("98765")
                             .build();

    @BeforeEach
    void setUp() {
        final Region 서울특별시 = new Region("서울특별시");
        final Region 강남구 = new Region("강남구");
        final Region 역삼동 = new Region("역삼동");
        강남구.addThirdRegion(역삼동);
        서울특별시.addSecondRegion(강남구);

        서울특별시_강남구_역삼동 = new AuctionRegion(서울특별시);

        경매_이미지 = new AuctionImage("image.png", "image.png");
        ReflectionTestUtils.setField(경매_이미지, "id", 1L);
        ReflectionTestUtils.setField(판매자, "id", 1L);
        ReflectionTestUtils.setField(구매자, "id", 2L);
        ReflectionTestUtils.setField(사용자, "id", 3L);
    }
}
