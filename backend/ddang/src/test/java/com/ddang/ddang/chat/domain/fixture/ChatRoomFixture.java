package com.ddang.ddang.chat.domain.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomFixture {

    protected User 판매자;
    protected User 구매자;
    protected User 경매에_참여하지_않는_사용자;
    protected Auction 경매;
    protected Bid 입찰;

    @BeforeEach
    void setUp() {
        판매자 = User.builder()
                   .name("판매자")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(new Reliability(4.7d))
                   .oauthId("12345")
                   .build();
        구매자 = User.builder()
                   .name("구매자")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(new Reliability(4.7d))
                   .oauthId("12346")
                   .build();
        경매에_참여하지_않는_사용자 = User.builder()
                                   .name("경매에 참여하지 않는 사용자")
                                   .profileImage(new ProfileImage("upload.png", "store.png"))
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12347")
                                   .build();

        경매 = Auction.builder()
                     .seller(판매자)
                     .title("맥북")
                     .description("맥북 팔아요")
                     .subCategory(new Category("전자기기"))
                     .startPrice(new Price(10_000))
                     .bidUnit(new BidUnit(1_000))
                     .closingTime(LocalDateTime.now())
                     .build();
        입찰 = new Bid(경매, 구매자, new BidPrice(15_000));

        ReflectionTestUtils.setField(판매자, "id", 1L);
        ReflectionTestUtils.setField(구매자, "id", 2L);
        ReflectionTestUtils.setField(경매에_참여하지_않는_사용자, "id", 3L);

        경매.updateLastBid(입찰);
    }
}
