package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.bid.infrastructure.persistence.BidRepositoryImpl;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import com.ddang.ddang.region.infrastructure.persistence.JpaRegionRepository;
import com.ddang.ddang.region.infrastructure.persistence.RegionRepositoryImpl;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionRepositoryImplFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    private AuctionRepository auctionRepository;

    private UserRepository userRepository;

    private RegionRepository regionRepository;

    private BidRepository bidRepository;

    private Instant 시간 = Instant.parse("2023-07-08T22:21:20Z");
    private ZoneId 위치 = ZoneId.of("UTC");

    protected Long 존재하지_않는_경매_id = -999L;
    protected ReadAuctionSearchCondition 검색어_없음 = new ReadAuctionSearchCondition(null);
    protected int 페이지_1 = 0;
    protected int 페이지_크기 = 3;
    protected Auction 저장하기_전_경매_엔티티 = Auction.builder()
                                             .title("제목")
                                             .description("내용")
                                             .bidUnit(new BidUnit(1_000))
                                             .startPrice(new Price(1_000))
                                             .closingTime(LocalDateTime.now())
                                             .build();
    protected Auction 저장된_경매_엔티티;
    protected Auction 삭제된_경매_엔티티;
    protected Auction 삭제된_경매만_있는_사용자의_삭제된_경매_엔티티;
    protected User 판매자;
    protected User 삭제한_경매를_갖고_있는_판매자;
    protected User 구매자;
    protected User 삭제된_경매의_마지막_입찰자;
    protected Bid 입찰;

    @BeforeEach
    void fixtureSetUp(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaRegionRepository jpaRegionRepository,
            @Autowired final JpaBidRepository jpaBidRepository
    ) {
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        regionRepository = new RegionRepositoryImpl(jpaRegionRepository);
        bidRepository = new BidRepositoryImpl(jpaBidRepository);

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

        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();
        삭제한_경매를_갖고_있는_판매자 = User.builder()
                                .name("판매자2")
                                .profileImage(new ProfileImage("upload.png", "store.png"))
                                .reliability(new Reliability(4.7d))
                                .oauthId("12346")
                                .build();

        구매자 = User.builder()
                  .name("구매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(2.7d))
                  .oauthId("54321")
                  .build();
        삭제된_경매의_마지막_입찰자 = User.builder()
                              .name("삭제된 경매의 마지막 입찰자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(2.7d))
                              .oauthId("54322")
                              .build();

        userRepository.save(판매자);
        userRepository.save(삭제한_경매를_갖고_있는_판매자);
        userRepository.save(구매자);
        userRepository.save(삭제된_경매의_마지막_입찰자);

        저장된_경매_엔티티 = Auction.builder()
                            .title("경매 상품 1")
                            .description("이것은 경매 상품 1 입니다.")
                            .bidUnit(new BidUnit(1_000))
                            .startPrice(new Price(1_000))
                            .closingTime(시간.atZone(위치).toLocalDateTime())
                            .subCategory(가구_서브_의자_카테고리)
                            .seller(판매자)
                            .build();
        삭제된_경매_엔티티 = Auction.builder()
                            .title("경매 상품 1")
                            .description("이것은 경매 상품 1 입니다.")
                            .bidUnit(new BidUnit(1_000))
                            .startPrice(new Price(1_000))
                            .closingTime(시간.atZone(위치).toLocalDateTime())
                            .subCategory(가구_서브_의자_카테고리)
                            .seller(판매자)
                            .build();
        삭제된_경매만_있는_사용자의_삭제된_경매_엔티티 = Auction.builder()
                                            .title("경매 상품 1")
                                            .description("이것은 경매 상품 1 입니다.")
                                            .bidUnit(new BidUnit(1_000))
                                            .startPrice(new Price(1_000))
                                            .closingTime(시간.atZone(위치).toLocalDateTime())
                                            .subCategory(가구_서브_의자_카테고리)
                                            .seller(삭제한_경매를_갖고_있는_판매자)
                                            .build();

        삭제된_경매_엔티티.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        저장된_경매_엔티티.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        삭제된_경매_엔티티.delete();
        삭제된_경매만_있는_사용자의_삭제된_경매_엔티티.addAuctionRegions(List.of(new AuctionRegion(역삼동)));
        삭제된_경매만_있는_사용자의_삭제된_경매_엔티티.delete();
        bidding(저장된_경매_엔티티, 구매자);
        addAuctioneerCount(저장된_경매_엔티티, 구매자, 1);
        bidding(삭제된_경매_엔티티, 삭제된_경매의_마지막_입찰자);
        addAuctioneerCount(삭제된_경매_엔티티, 삭제된_경매의_마지막_입찰자, 1);

        auctionRepository.save(삭제된_경매_엔티티);
        auctionRepository.save(저장된_경매_엔티티);
        auctionRepository.save(삭제된_경매만_있는_사용자의_삭제된_경매_엔티티);
    }

    private void bidding(final Auction targetAuction, final User bidder) {
        final Bid lastBid = new Bid(targetAuction, bidder, new BidPrice(1));

        bidRepository.save(lastBid);

        targetAuction.updateLastBid(lastBid);
    }

    private void addAuctioneerCount(final Auction targetAuction, final User bidder, final int count) {
        final Bid lastBid = new Bid(targetAuction, bidder, new BidPrice(1));

        bidRepository.save(lastBid);

        for (int i = 0; i < count; i++) {
            targetAuction.updateLastBid(lastBid);
        }
    }
}
