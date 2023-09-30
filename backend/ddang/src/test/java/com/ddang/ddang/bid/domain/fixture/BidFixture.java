package com.ddang.ddang.bid.domain.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class BidFixture {

    protected User 판매자;
    protected Auction 경매;
    protected User 입찰자;
    protected BidPrice 입찰액;
    protected BidPrice 이전_입찰액보다_작은_입찰액;
    protected BidPrice 이전_입찰액보다_크지만_입찰_단위보다_작은_입찰액;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        경매 = Auction.builder()
                    .seller(판매자)
                    .title("경매 상품")
                    .description("이것은 경매 상품입니다.")
                    .subCategory(전자기기_서브_노트북_카테고리)
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now())
                    .build();
        경매.addAuctionImages(List.of(경매_이미지));

        입찰자 = User.builder()
                  .name("입찰자")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12346")
                  .build();

        입찰액 = new BidPrice(10_000);
        이전_입찰액보다_작은_입찰액 = new BidPrice(9_000);
        이전_입찰액보다_크지만_입찰_단위보다_작은_입찰액 = new BidPrice(10_900);
    }
}
