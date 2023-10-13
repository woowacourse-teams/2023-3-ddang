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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaAuctionRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaRegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    private Instant 시간 = Instant.parse("2023-07-08T22:21:20Z");
    private ZoneId 위치 = ZoneId.of("UTC");

    protected Auction 저장하기_전_경매_엔티티 = Auction.builder()
                                             .title("제목")
                                             .description("내용")
                                             .bidUnit(new BidUnit(1_000))
                                             .startPrice(new Price(1_000))
                                             .closingTime(LocalDateTime.now())
                                             .build();
    protected Auction 저장된_경매_엔티티;
    protected Auction 삭제된_경매_엔티티;

    @BeforeEach
    void setUp() {
        final Region 서울특별시 = new Region("서울특별시");
        final Region 강남구 = new Region("강남구");
        final Region 역삼동 = new Region("역삼동");

        서울특별시.addSecondRegion(강남구);
        강남구.addThirdRegion(역삼동);

        regionRepository.save(서울특별시);

        final Category 가구_카테고리 = new Category("가구");
        final Category 가구_서브_의자_카테고리 = new Category("의자");

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);

        categoryRepository.save(가구_카테고리);

        final User 사용자 = User.builder()
                             .name("사용자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(new Reliability(4.7d))
                             .oauthId("12345")
                             .build();

        userRepository.save(사용자);

        저장된_경매_엔티티 = Auction.builder()
                            .title("경매 상품 1")
                            .description("이것은 경매 상품 1 입니다.")
                            .bidUnit(new BidUnit(1_000))
                            .startPrice(new Price(1_000))
                            .closingTime(시간.atZone(위치).toLocalDateTime())
                            .subCategory(가구_서브_의자_카테고리)
                            .seller(사용자)
                            .build();
        삭제된_경매_엔티티 = Auction.builder()
                            .title("경매 상품 1")
                            .description("이것은 경매 상품 1 입니다.")
                            .bidUnit(new BidUnit(1_000))
                            .startPrice(new Price(1_000))
                            .closingTime(시간.atZone(위치).toLocalDateTime())
                            .subCategory(가구_서브_의자_카테고리)
                            .seller(사용자)
                            .build();

        삭제된_경매_엔티티.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        저장된_경매_엔티티.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        삭제된_경매_엔티티.delete();

        auctionRepository.saveAll(List.of(저장된_경매_엔티티, 삭제된_경매_엔티티));

        em.flush();
        em.clear();
    }
}
