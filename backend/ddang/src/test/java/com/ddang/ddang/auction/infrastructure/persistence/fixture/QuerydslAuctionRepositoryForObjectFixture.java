package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslAuctionRepositoryForObjectFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaAuctionRepository auctionRepository;

    @Autowired
    JpaRegionRepository regionRepository;

    @Autowired
    JpaCategoryRepository categoryRepository;

    protected Auction 경매;
    protected Region 서울특별시;
    protected Region 강남구;
    protected Region 개포1동;
    protected Category 가구_카테고리;
    protected Category 가구_서브_의자_카테고리;
    protected User 판매자;

    @BeforeEach
    void totalFixtureSetUp() {
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        userRepository.save(판매자);

        가구_카테고리 = new Category("가구");
        가구_서브_의자_카테고리 = new Category("의자");

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);

        categoryRepository.save(가구_카테고리);

        경매 = Auction.builder()
                    .title("제목")
                    .description("설명")
                    .bidUnit(new BidUnit(1_000))
                    .startPrice(new Price(1_000))
                    .closingTime(LocalDateTime.now())
                    .subCategory(가구_서브_의자_카테고리)
                    .seller(판매자)
                    .build();

        서울특별시 = new Region("서울특별시");
        강남구 = new Region("강남구");
        개포1동 = new Region("개포1동");

        강남구.addThirdRegion(개포1동);
        서울특별시.addSecondRegion(강남구);

        regionRepository.save(서울특별시);
        final AuctionRegion 직거래_지역 = new AuctionRegion(개포1동);

        경매.addAuctionRegions(List.of(직거래_지역));

        auctionRepository.save(경매);
    }
}
