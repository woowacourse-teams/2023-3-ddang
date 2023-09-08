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

    @BeforeEach
    void setUp() {
        querydslAuctionRepository = new QuerydslAuctionRepositoryImpl(new JPAQueryFactory(em));

        final User seller1 = User.builder()
                                 .name("회원1234543211")
                                 .profileImage("profile.png")
                                 .reliability(4.7d)
                                 .oauthId("1234543211")
                                 .build();
        final User seller2 = User.builder()
                                 .name("회원1234543212")
                                 .profileImage("profile.png")
                                 .reliability(3.5d)
                                 .oauthId("1234543212")
                                 .build();
        final User seller3 = User.builder()
                                 .name("회원1234543213")
                                 .profileImage("profile.png")
                                 .reliability(2.1d)
                                 .oauthId("1234543213")
                                 .build();
        final User seller4 = User.builder()
                                 .name("회원1234543214")
                                 .profileImage("profile.png")
                                 .reliability(5.0d)
                                 .oauthId("1234543214")
                                 .build();
        final User seller5 = User.builder()
                                 .name("회원1234543215")
                                 .profileImage("profile.png")
                                 .reliability(1.5d)
                                 .oauthId("1234543215")
                                 .build();
        final User seller6 = User.builder()
                                 .name("회원1234543216")
                                 .profileImage("profile.png")
                                 .reliability(0.3d)
                                 .oauthId("1234543216")
                                 .build();

        final List<User> users = List.of(seller1, seller2, seller3, seller4, seller5, seller6);
        userRepository.saveAll(users);

        final Category main = new Category("main");
        final Category sub = new Category("sub");

        main.addSubCategory(sub);

        categoryRepository.save(main);

        auction1 = Auction.builder()
                          .title("맥북에어를 팝니다")
                          .description("맥북에어를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(5))
                          .seller(seller1)
                          .build();
        addAuctioneerCount(auction1, 2);
        auction2 = Auction.builder()
                          .title("맥북프로를 팝니다")
                          .description("맥북프로를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(4))
                          .seller(seller2)
                          .build();
        addAuctioneerCount(auction2, 1);
        auction3 = Auction.builder()
                          .title("맥북뭐시기를 팝니다")
                          .description("맥북뭐시기를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(3))
                          .seller(seller3)
                          .build();
        addAuctioneerCount(auction3, 4);
        auction4 = Auction.builder()
                          .title("집에가고싶은 맥북을 팝니다")
                          .description("집에가고싶은 맥북을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(2))
                          .seller(seller4)
                          .build();
        addAuctioneerCount(auction4, 7);
        auction5 = Auction.builder()
                          .title("핫식스를 팝니다")
                          .description("핫식스를 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(1))
                          .seller(seller5)
                          .build();
        addAuctioneerCount(auction5, 4);
        auction6 = Auction.builder()
                          .title("레드불을 팝니다")
                          .description("레드불을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(2))
                          .seller(seller6)
                          .build();
        addAuctioneerCount(auction6, 8);
        auction7 = Auction.builder()
                          .title("맥북1을 팝니다")
                          .description("맥북1을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(3))
                          .seller(seller1)
                          .build();
        addAuctioneerCount(auction7, 3);
        auction8 = Auction.builder()
                          .title("맥북2을 팝니다")
                          .description("맥북2을 팝니다")
                          .bidUnit(new BidUnit(1_000))
                          .startPrice(new Price(1_000))
                          .closingTime(LocalDateTime.now().plusDays(4))
                          .seller(seller2)
                          .build();
        addAuctioneerCount(auction8, 6);

        final List<Auction> auctions = List.of(auction1, auction2, auction3, auction4, auction5, auction6, auction7,
                auction8);
        auctionRepository.saveAll(auctions);

        em.flush();
        em.clear();
    }

    private void addAuctioneerCount(final Auction targetAuction, final int count) {
        final Bid lastBid = new Bid(targetAuction, targetAuction.getSeller(), new BidPrice(1));

        bidRepository.save(lastBid);

        for(int i = 0; i < count; i++) {
            targetAuction.updateLastBid(lastBid);
        }
    }
}
