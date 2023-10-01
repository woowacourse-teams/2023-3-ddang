package com.ddang.ddang.bid.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class BidServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    protected String 알림_성공_메시지 = "성공";
    protected String 이미지_절대_url = "https://3-ddang.store/auctions/images";
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Long 존재하지_않는_사용자_아이디 = -9999L;

    protected User 입찰자1;
    protected User 입찰자2;
    protected Auction 경매1;
    protected Auction 입찰_내역이_없는_경매;

    protected CreateBidDto 입찰_요청_dto1;
    protected CreateBidDto 입찰_요청_dto2;
    protected CreateBidDto 시작가로_입찰_요청_dto;
    protected CreateBidDto 존재하지_않는_경매_아이디에_대한_입찰_요청_dto;
    protected CreateBidDto 존재하지_않는_사용자_아이디를_통한_입찰_요청_dto;
    protected CreateBidDto 종료된_경매에_대한_입찰_요청_dto;
    protected CreateBidDto 삭제된_경매에_대한_입찰_요청_dto;
    protected CreateBidDto 판매자가_본인_경매에_입찰_요청_dto;
    protected CreateBidDto 첫입찰시_시작가보다_낮은_입찰액으로_입찰_요청_dto;
    protected CreateBidDto 동일한_사용자가_입찰_요청_dto;
    protected CreateBidDto 이전_입찰액보다_낮은_입찰액으로_입찰_요청_dto;
    protected CreateBidDto 최소_입찰단위를_더한_금액보다_낮은_입찰액으로_입찰_요청_dto;
    protected static CreateBidDto 범위_밖의_금액으로_입찰_요청_dto1;
    protected static CreateBidDto 범위_밖의_금액으로_입찰_요청_dto2;

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");
        final User 판매자 = User.builder()
                             .name("판매자")
                             .profileImage(프로필_이미지)
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
        입찰자1 = User.builder()
                   .name("구매자1")
                   .profileImage(프로필_이미지)
                   .reliability(4.7d)
                   .oauthId("12346")
                   .build();
        입찰자2 = User.builder()
                   .name("구매자2")
                   .profileImage(프로필_이미지)
                   .reliability(4.7d)
                   .oauthId("78910")
                   .build();
        final AuctionImage 경매_이미지1 = new AuctionImage("auction_image.png", "auction_image.png");
        경매1 = Auction.builder()
                     .seller(판매자)
                     .title("경매 상품 1")
                     .description("이것은 경매 상품 1 입니다.")
                     .bidUnit(new BidUnit(1_000))
                     .startPrice(new Price(1_000))
                     .closingTime(LocalDateTime.now().plusDays(7))
                     .build();
        final AuctionImage 경매_이미지2 = new AuctionImage("auction_image.png", "auction_image.png");
        final Auction 경매2 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품 2")
                                   .description("이것은 경매 상품 2 입니다.")
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now().plusDays(7))
                                   .build();
        final AuctionImage 경매_이미지3 = new AuctionImage("auction_image.png", "auction_image.png");
        final Auction 경매3 = Auction.builder()
                                   .seller(판매자)
                                   .title("경매 상품 2")
                                   .description("이것은 경매 상품 2 입니다.")
                                   .bidUnit(new BidUnit(1_000))
                                   .startPrice(new Price(1_000))
                                   .closingTime(LocalDateTime.now().plusDays(7))
                                   .build();
        입찰_내역이_없는_경매 = 경매3;
        final Auction 종료된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 2")
                                      .description("이것은 경매 상품 2 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().minusDays(7))
                                      .build();
        final Auction 삭제된_경매 = Auction.builder()
                                      .seller(판매자)
                                      .title("경매 상품 2")
                                      .description("이것은 경매 상품 2 입니다.")
                                      .bidUnit(new BidUnit(1_000))
                                      .startPrice(new Price(1_000))
                                      .closingTime(LocalDateTime.now().plusDays(7))
                                      .build();
        삭제된_경매.delete();

        userRepository.saveAll(List.of(판매자, 입찰자1, 입찰자2));
        경매1.addAuctionImages(List.of(경매_이미지1));
        경매2.addAuctionImages(List.of(경매_이미지2));
        경매3.addAuctionImages(List.of(경매_이미지3));
        auctionRepository.saveAll(List.of(경매1, 경매2, 경매3, 종료된_경매, 삭제된_경매));

        final Bid bid1 = new Bid(경매1, 입찰자1, new BidPrice(1_000));
        final Bid bid2 = new Bid(경매2, 입찰자1, new BidPrice(1_000));
        final Bid bid3 = new Bid(경매1, 입찰자2, new BidPrice(10_000));
        bidRepository.save(bid1);
        bidRepository.save(bid2);
        bidRepository.save(bid3);

        경매1.updateLastBid(bid1);
        경매2.updateLastBid(bid2);
        경매1.updateLastBid(bid3);

        입찰_요청_dto1 = new CreateBidDto(경매3.getId(), 10_000, 입찰자1.getId());
        입찰_요청_dto2 = new CreateBidDto(경매3.getId(), 14_000, 입찰자2.getId());
        시작가로_입찰_요청_dto = new CreateBidDto(경매3.getId(), 1_000, 입찰자1.getId());
        존재하지_않는_경매_아이디에_대한_입찰_요청_dto = new CreateBidDto(존재하지_않는_경매_아이디, 10_000, 입찰자1.getId());
        존재하지_않는_사용자_아이디를_통한_입찰_요청_dto = new CreateBidDto(경매3.getId(), 10_000, 존재하지_않는_사용자_아이디);
        종료된_경매에_대한_입찰_요청_dto = new CreateBidDto(종료된_경매.getId(), 10_000, 입찰자1.getId());
        삭제된_경매에_대한_입찰_요청_dto = new CreateBidDto(삭제된_경매.getId(), 10_000, 입찰자1.getId());
        판매자가_본인_경매에_입찰_요청_dto = new CreateBidDto(경매3.getId(), 10_000, 판매자.getId());
        첫입찰시_시작가보다_낮은_입찰액으로_입찰_요청_dto = new CreateBidDto(경매3.getId(), 900, 입찰자1.getId());
        동일한_사용자가_입찰_요청_dto = new CreateBidDto(경매3.getId(), 12_000, 입찰자1.getId());
        이전_입찰액보다_낮은_입찰액으로_입찰_요청_dto = new CreateBidDto(경매3.getId(), 8_000, 입찰자2.getId());
        최소_입찰단위를_더한_금액보다_낮은_입찰액으로_입찰_요청_dto = new CreateBidDto(경매3.getId(), 10_500, 입찰자2.getId());
        범위_밖의_금액으로_입찰_요청_dto1 = new CreateBidDto(경매3.getId(), -1, 입찰자2.getId());
        범위_밖의_금액으로_입찰_요청_dto2 = new CreateBidDto(경매3.getId(), 2_100_000_001, 입찰자2.getId());
    }
}
