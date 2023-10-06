package com.ddang.ddang.bid.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
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
public class JpaBidRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    protected User 판매자;
    protected Auction 경매1;
    protected Auction 경매2;
    protected User 입찰자1;
    protected BidPrice 입찰액;
    protected Bid 경매1의_입찰1;
    protected Bid 경매1의_입찰2겸_마지막_입찰;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필.jpg", "프로필.jpg");
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7f))
                  .oauthId("12345")
                  .build();

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        final AuctionImage 경매_이미지 = new AuctionImage("경매이미지.jpg", "경매이미지.jpg");
        경매1 = Auction.builder()
                     .seller(판매자)
                     .title("경매 상품")
                     .description("이것은 경매 상품입니다.")
                     .subCategory(전자기기_서브_노트북_카테고리)
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(LocalDateTime.now())
                     .build();
        경매1.addAuctionImages(List.of(경매_이미지));
        경매2 = Auction.builder()
                     .seller(판매자)
                     .title("경매 상품")
                     .description("이것은 경매 상품입니다.")
                     .subCategory(전자기기_서브_노트북_카테고리)
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(LocalDateTime.now())
                     .build();
        경매2.addAuctionImages(List.of(경매_이미지));

        입찰자1 = User.builder()
                   .name("입찰자1")
                   .profileImage(프로필_이미지)
                   .reliability(new Reliability(4.7f))
                   .oauthId("12346")
                   .build();
        final User 입찰자2 = User.builder()
                              .name("입찰자2")
                              .profileImage(프로필_이미지)
                              .reliability(new Reliability(4.7f))
                              .oauthId("12346")
                              .build();

        입찰액 = new BidPrice(10_000);

        경매1의_입찰1 = new Bid(경매1, 입찰자1, 입찰액);
        경매1의_입찰2겸_마지막_입찰 = new Bid(경매1, 입찰자2, 입찰액);
        final Bid 경매2의_입찰1 = new Bid(경매2, 입찰자1, 입찰액);

        userRepository.saveAll(List.of(판매자, 입찰자1, 입찰자2));

        categoryRepository.saveAll(List.of(전자기기_카테고리, 전자기기_서브_노트북_카테고리));
        auctionRepository.saveAll(List.of(경매1, 경매2));

        bidRepository.saveAll(List.of(경매1의_입찰1, 경매1의_입찰2겸_마지막_입찰, 경매2의_입찰1));

        em.flush();
        em.clear();
    }
}
