package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
public class InitializeCommonAuctionData extends QuerydslAuctionRepositoryImplTest {

    @Autowired
    JpaBidRepository bidRepository;

    Auction auction1;
    Auction auction2;
    Auction auction3;
    Auction auction4;
    Auction auction5;
    Auction auction6;
    Auction auction7;
    Auction auction8;
    Auction auction9;
    Auction auction10;
    Auction auction11;
    Auction auction12;
    Auction auction13;
    Auction auction14;
    Auction auction15;
    Auction auction16;

    User seller1;
    User seller2;
    User seller3;
    User seller4;
    User seller5;
    User seller6;
    User seller7;

    @BeforeEach
    void setUp() {
        querydslAuctionRepository = new QuerydslAuctionRepositoryImpl(new JPAQueryFactory(em));

        seller1 = User.builder()
                                 .name("회원1234543211")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(4.7d)
                                 .oauthId("1234543211")
                                 .build();
        seller2 = User.builder()
                                 .name("회원1234543212")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(3.5d)
                                 .oauthId("1234543212")
                                 .build();
        seller3 = User.builder()
                                 .name("회원1234543213")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(2.1d)
                                 .oauthId("1234543213")
                                 .build();
        seller4 = User.builder()
                                 .name("회원1234543214")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(5.0d)
                                 .oauthId("1234543214")
                                 .build();
        seller5 = User.builder()
                                 .name("회원1234543215")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(1.5d)
                                 .oauthId("1234543215")
                                 .build();
        seller6 = User.builder()
                                 .name("회원1234543216")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(0.3d)
                                 .oauthId("1234543216")
                                 .build();
        seller7 = User.builder()
                      .name("회원1234543217")
                      .profileImage(new ProfileImage("upload.png", "store.png"))
                      .reliability(0.3d)
                      .oauthId("1234543217")
                      .build();

        final List<User> users = List.of(seller1, seller2, seller3, seller4, seller5, seller6, seller7);
        userRepository.saveAll(users);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);

        categoryRepository.save(main);

        final LocalDateTime nowTime = LocalDateTime.now();

        auction6 = Auction.builder()
                          .title("레드불을 팝니다")
                          .description("레드불을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(2))
                          .seller(seller6)
                          .build();
        bidding(auction6, seller1);
        addAuctioneerCount(auction6, 7);
        auction8 = Auction.builder()
                          .title("맥북을 팝니다")
                          .description("맥북을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(4))
                          .seller(seller2)
                          .build();
        bidding(auction8, seller1);
        addAuctioneerCount(auction8, 5);
        auction16 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        bidding(auction16, seller1);
        addAuctioneerCount(auction16, 4);
        auction9 = Auction.builder()
                          .title("맥북을 팝니다")
                          .description("맥북을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(4))
                          .seller(seller2)
                          .build();
        bidding(auction9, seller1);
        addAuctioneerCount(auction9, 5);
        auction13 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        bidding(auction13, seller1);
        addAuctioneerCount(auction13, 5);
        auction7 = Auction.builder()
                          .title("맥북을 팝니다")
                          .description("맥북1을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(3))
                          .seller(seller1)
                          .build();
        addAuctioneerCount(auction7, 3);
        auction1 = Auction.builder()
                          .title("맥북에어를 팝니다")
                          .description("맥북에어를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(5))
                          .seller(seller1)
                          .build();
        addAuctioneerCount(auction1, 2);
        bidding(auction16, seller1);

        auction2 = Auction.builder()
                          .title("맥북프로를 팝니다")
                          .description("맥북프로를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(4))
                          .seller(seller2)
                          .build();
        addAuctioneerCount(auction2, 1);
        auction3 = Auction.builder()
                          .title("맥북뭐시기를 팝니다")
                          .description("맥북뭐시기를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(3))
                          .seller(seller3)
                          .build();
        addAuctioneerCount(auction3, 4);
        auction4 = Auction.builder()
                          .title("집에가고싶은 맥북을 팝니다")
                          .description("집에가고싶은 맥북을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(2))
                          .seller(seller4)
                          .build();
        addAuctioneerCount(auction4, 7);
        auction5 = Auction.builder()
                          .title("핫식스를 팝니다")
                          .description("핫식스를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(nowTime.plusDays(1))
                          .seller(seller5)
                          .build();
        addAuctioneerCount(auction5, 4);
        auction10 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        addAuctioneerCount(auction10, 6);
        auction11 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        addAuctioneerCount(auction11, 6);
        auction12 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        addAuctioneerCount(auction12, 6);
        auction14 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        addAuctioneerCount(auction14, 6);
        auction15 = Auction.builder()
                           .title("맥북을 팝니다")
                           .description("맥북을 팝니다")
                           .bidUnit(new BidUnit(1_000))
                           .startPrice(new Price(1_000))
                           .closingTime(nowTime.plusDays(4))
                           .seller(seller2)
                           .build();
        addAuctioneerCount(auction15, 6);

        final List<Auction> auctions = List.of(
                auction1, auction2, auction3, auction4, auction5, auction6, auction7, auction8, auction9,
                auction10, auction11, auction12, auction13, auction14, auction15, auction16
        );
        auctionRepository.saveAll(auctions);

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
