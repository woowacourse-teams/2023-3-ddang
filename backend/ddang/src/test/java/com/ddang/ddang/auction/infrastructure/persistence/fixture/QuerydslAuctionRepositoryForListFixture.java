package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslAuctionRepositoryForListFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private JpaAuctionRepository jpaAuctionRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    protected User 판매자_4_7점;
    protected User 판매자_3_5점;
    protected User 판매자_2_1점;
    protected User 판매자_5_0점;
    protected User 판매자_1_5점;
    protected User 판매자_0_3점_1;
    protected User 판매자_0_3점_2;

    protected Auction 경매1;
    protected Auction 경매2;
    protected Auction 경매3;
    protected Auction 경매4;
    protected Auction 경매5;
    protected Auction 경매6;
    protected Auction 경매7;
    protected Auction 경매8;
    protected Auction 경매9;
    protected Auction 경매10;
    protected Auction 경매11;
    protected Auction 경매12;
    protected Auction 경매13;
    protected Auction 경매14;
    protected Auction 경매15;
    protected Auction 경매16;

    protected int 페이지_크기_3 = 3;

    @BeforeEach
    void commonFixtureSetUp() {
        판매자_4_7점 = User.builder()
                       .name("판매자 4.7점")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(4.7d))
                       .oauthId("1")
                       .build();
        판매자_3_5점 = User.builder()
                       .name("판매자 3.5점")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(3.5d))
                       .oauthId("2")
                       .build();
        판매자_2_1점 = User.builder()
                       .name("판매자 2.1점 ")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(2.1d))
                       .oauthId("3")
                       .build();
        판매자_5_0점 = User.builder()
                       .name("판매자 5.0점")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(5.0d))
                       .oauthId("4")
                       .build();
        판매자_1_5점 = User.builder()
                       .name("판매자 1.5점")
                       .profileImage(new ProfileImage("upload.png", "store.png"))
                       .reliability(new Reliability(1.5d))
                       .oauthId("5")
                       .build();
        판매자_0_3점_1 = User.builder()
                         .name("판매자 0.3점 1")
                         .profileImage(new ProfileImage("upload.png", "store.png"))
                         .reliability(new Reliability(0.3d))
                         .oauthId("6")
                         .build();
        판매자_0_3점_2 = User.builder()
                         .name("판매자 0.3점 2")
                         .profileImage(new ProfileImage("upload.png", "store.png"))
                         .reliability(new Reliability(0.3d))
                         .oauthId("7")
                         .build();

        userRepository.saveAll(List.of(판매자_4_7점, 판매자_3_5점, 판매자_2_1점, 판매자_5_0점, 판매자_1_5점, 판매자_0_3점_1, 판매자_0_3점_2));

        final Category 기타_카테고리 = new Category("기타");
        final Category 기타_서브_기타_카테고리 = new Category("기타");

        기타_카테고리.addSubCategory(기타_서브_기타_카테고리);

        categoryRepository.save(기타_카테고리);

        final LocalDateTime 현재시간 = LocalDateTime.now();

        경매6 = Auction.builder()
                     .title("레드불을 팝니다")
                     .description("레드불을 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.minusDays(2))
                     .seller(판매자_0_3점_1)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        bidding(경매6, 판매자_4_7점);
        addAuctioneerCount(경매6, 7);
        경매8 = Auction.builder()
                     .title("맥북을 팝니다")
                     .description("맥북을 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.minusDays(4))
                     .seller(판매자_3_5점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        bidding(경매8, 판매자_4_7점);
        addAuctioneerCount(경매8, 5);
        경매16 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.minusDays(4))
                      .seller(판매자_3_5점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        bidding(경매16, 판매자_4_7점);
        addAuctioneerCount(경매16, 4);
        경매9 = Auction.builder()
                     .title("맥북을 팝니다")
                     .description("맥북을 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.minusDays(4))
                     .seller(판매자_2_1점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        bidding(경매9, 판매자_4_7점);
        addAuctioneerCount(경매9, 5);
        경매13 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.minusDays(4))
                      .seller(판매자_2_1점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        bidding(경매13, 판매자_4_7점);
        addAuctioneerCount(경매13, 5);
        경매7 = Auction.builder()
                     .title("맥북을 팝니다")
                     .description("맥북1을 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.minusDays(3))
                     .seller(판매자_4_7점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매7, 3);
        경매1 = Auction.builder()
                     .title("맥북에어를 팝니다")
                     .description("맥북에어를 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.minusDays(5))
                     .seller(판매자_4_7점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매1, 2);
        bidding(경매16, 판매자_4_7점);
        경매2 = Auction.builder()
                     .title("맥북프로를 팝니다")
                     .description("맥북프로를 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.plusDays(4))
                     .seller(판매자_2_1점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매2, 1);
        경매3 = Auction.builder()
                     .title("맥북뭐시기를 팝니다")
                     .description("맥북뭐시기를 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.plusDays(3))
                     .seller(판매자_2_1점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매3, 4);
        경매4 = Auction.builder()
                     .title("집에가고싶은 맥북을 팝니다")
                     .description("집에가고싶은 맥북을 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.plusDays(2))
                     .seller(판매자_5_0점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매4, 7);
        경매5 = Auction.builder()
                     .title("핫식스를 팝니다")
                     .description("핫식스를 팝니다")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(현재시간.plusDays(1))
                     .seller(판매자_1_5점)
                     .subCategory(기타_서브_기타_카테고리)
                     .build();
        addAuctioneerCount(경매5, 4);
        경매10 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.plusDays(4))
                      .seller(판매자_3_5점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        addAuctioneerCount(경매10, 6);
        경매11 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.plusDays(4))
                      .seller(판매자_2_1점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        addAuctioneerCount(경매11, 6);
        경매12 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.plusDays(4))
                      .seller(판매자_3_5점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        addAuctioneerCount(경매12, 6);
        경매14 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.plusDays(4))
                      .seller(판매자_3_5점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        addAuctioneerCount(경매14, 6);
        경매15 = Auction.builder()
                      .title("맥북을 팝니다")
                      .description("맥북을 팝니다")
                      .bidUnit(new BidUnit(1_000))
                      .startPrice(new Price(1_000))
                      .closingTime(현재시간.plusDays(4))
                      .seller(판매자_2_1점)
                      .subCategory(기타_서브_기타_카테고리)
                      .build();
        addAuctioneerCount(경매15, 6);

        final AuctionRepository auctionRepository = new AuctionRepositoryImpl(
                jpaAuctionRepository,
                new QuerydslAuctionRepository(queryFactory)
        );

        auctionRepository.save(경매1);
        auctionRepository.save(경매2);
        auctionRepository.save(경매3);
        auctionRepository.save(경매4);
        auctionRepository.save(경매5);
        auctionRepository.save(경매6);
        auctionRepository.save(경매7);
        auctionRepository.save(경매8);
        auctionRepository.save(경매9);
        auctionRepository.save(경매10);
        auctionRepository.save(경매11);
        auctionRepository.save(경매12);
        auctionRepository.save(경매13);
        auctionRepository.save(경매14);
        auctionRepository.save(경매15);
        auctionRepository.save(경매16);

        em.flush();
        em.clear();
    }

    private void bidding(final Auction targetAuction, final User bidder) {
        final Bid lastBid = new Bid(targetAuction, bidder, new BidPrice(1));

        bidRepository.save(lastBid);

        targetAuction.updateLastBid(lastBid);
    }

    private void addAuctioneerCount(final Auction targetAuction, final int count) {
        final Bid lastBid = new Bid(targetAuction, targetAuction.getSeller(), new BidPrice(1));

        bidRepository.save(lastBid);

        for (int i = 0; i < count; i++) {
            targetAuction.updateLastBid(lastBid);
        }
    }
}
