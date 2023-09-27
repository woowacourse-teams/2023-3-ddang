package com.ddang.ddang.bid.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@IsolateDatabase
public class BidServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    // TODO: 2023/09/27 로컬입니까 url입니까?
    protected String 이미지_절대_경로_url = "https://3-ddang.store/auctions/images";
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Long 존재하지_않는_사용자_아이디 = -9999L;


    protected User 판매자;
    protected User 구매자1;
    protected User 구매자2;
    protected Auction 경매1;
    private Auction 경매2;
    protected AuctionImage 경매_이미지1;
    protected AuctionImage 경매_이미지2;

    @BeforeEach
    void setUp() {
        판매자 = User.builder()
                  .name("판매자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();
        구매자1 = User.builder()
                   .name("구매자1")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(4.7d)
                   .oauthId("12346")
                   .build();
        구매자2 = User.builder()
                   .name("구매자2")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(4.7d)
                   .oauthId("78910")
                   .build();
        경매_이미지1 = new AuctionImage("auction_image.png", "auction_image.png");
        경매1 = Auction.builder()
                     .seller(판매자)
                     .title("경매 상품 1")
                     .description("이것은 경매 상품 1 입니다.")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(LocalDateTime.now().plusDays(7))
                     .build();
        경매_이미지2 = new AuctionImage("auction_image.png", "auction_image.png");
        경매2 = Auction.builder()
                     .seller(판매자)
                     .title("경매 상품 2")
                     .description("이것은 경매 상품 2 입니다.")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(LocalDateTime.now().plusDays(7))
                     .build();

        userRepository.saveAll(List.of(판매자, 구매자1, 구매자2));
        auctionImageRepository.save(경매_이미지1);
        경매1.addAuctionImages(List.of(경매_이미지1));
        auctionRepository.save(경매1);
        auctionImageRepository.save(경매_이미지2);
        경매2.addAuctionImages(List.of(경매_이미지2));
        auctionRepository.save(경매2);

        final Bid bid1 = new Bid(경매1, 구매자1, new BidPrice(1_000));
        final Bid bid2 = new Bid(경매2, 구매자1, new BidPrice(1_000));
        final Bid bid3 = new Bid(경매1, 구매자2, new BidPrice(10_000));
        bidRepository.save(bid1);
        bidRepository.save(bid2);
        bidRepository.save(bid3);

        경매1.updateLastBid(bid1);
        경매2.updateLastBid(bid2);
        경매1.updateLastBid(bid3);
    }
}
