package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaMessageRepository messageRepository;

    private Category 전자기기_카테고리 = new Category("전자기기");
    private Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
    private ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected User 판매자 = User.builder()
                           .name("판매자")
                           .profileImage(프로필_이미지)
                           .reliability(4.7d)
                           .oauthId("12345")
                           .build();
    protected User 구매자 = User.builder()
                           .name("구매자")
                           .profileImage(프로필_이미지)
                           .reliability(4.7d)
                           .oauthId("12346")
                           .build();
    private AuctionImage 경매_대표_이미지 = new AuctionImage("경매_대표_이미지.png", "경매_대표_이미지.png");
    private AuctionImage 대표_이미지가_아닌_경매_이미지 =
            new AuctionImage("대표 이미지가_아닌_경매_이미지.png", "대표 이미지가_아닌_경매_이미지.png");
    protected Long 존재하지_않는_경매_아이디 = -999L;
    protected Auction 채팅방이_없는_경매 = Auction.builder()
                                        .seller(판매자)
                                        .title("맥북")
                                        .description("맥북 팔아요")
                                        .subCategory(전자기기_서브_노트북_카테고리)
                                        .startPrice(new Price(10_000))
                                        .bidUnit(new BidUnit(1_000))
                                        .closingTime(LocalDateTime.now())
                                        .build();
    protected Auction 종료되지_않은_경매 = Auction.builder()
                                          .seller(판매자)
                                          .title("맥북")
                                          .description("맥북 팔아요")
                                          .subCategory(전자기기_서브_노트북_카테고리)
                                          .startPrice(new Price(10_000))
                                          .bidUnit(new BidUnit(1_000))
                                          .closingTime(LocalDateTime.now().plusDays(10L))
                                          .build();
    protected Auction 낙찰자가_없는_경매 = Auction.builder()
                                          .seller(판매자)
                                          .title("맥북")
                                          .description("맥북 팔아요")
                                          .subCategory(전자기기_서브_노트북_카테고리)
                                          .startPrice(new Price(10_000))
                                          .bidUnit(new BidUnit(1_000))
                                          .closingTime(LocalDateTime.now())
                                          .build();
    private Bid 채팅방_없는_경매_입찰 = new Bid(채팅방이_없는_경매, 구매자, new BidPrice(15_000));

    protected User 엔초 = User.builder()
                            .name("엔초")
                            .profileImage(프로필_이미지)
                            .reliability(4.7d)
                            .oauthId("12346")
                            .build();
    protected User 제이미 = User.builder()
                           .name("제이미")
                           .profileImage(프로필_이미지)
                           .reliability(4.7d)
                           .oauthId("12347")
                           .build();
    protected User 지토 = User.builder()
                          .name("지토")
                          .profileImage(프로필_이미지)
                          .reliability(4.7d)
                          .oauthId("12348")
                          .build();
    protected AuctionImage 엔초의_경매_대표_이미지 = new AuctionImage("엔초의_경매_대표_이미지.png", "엔초의_경매_대표_이미지.png");
    private AuctionImage 엔초의_대표_이미지가_아닌_경매_이미지 =
            new AuctionImage("엔초의_대표 이미지가_아닌_경매_이미지.png", "엔초의_대표 이미지가_아닌_경매_이미지.png");
    protected AuctionImage 제이미의_경매_대표_이미지 = new AuctionImage("제이미의_경매_대표_이미지.png", "제이미의_경매_대표_이미지.png");
    private AuctionImage 제이미의_대표_이미지가_아닌_경매_이미지 =
            new AuctionImage("제이미의_대표 이미지가_아닌_경매_이미지.png", "제이미의_대표 이미지가_아닌_경매_이미지.png");
    protected Auction 판매자_엔초_구매자_지토_경매 = Auction.builder()
                                              .seller(엔초)
                                              .title("엔초 맥북")
                                              .description("엔초 맥북 팔아요")
                                              .subCategory(전자기기_서브_노트북_카테고리)
                                              .startPrice(new Price(10_000))
                                              .bidUnit(new BidUnit(1_000))
                                              .closingTime(LocalDateTime.now())
                                              .build();
    protected Auction 판매자_제이미_구매자_엔초_경매 = Auction.builder()
                                               .seller(제이미)
                                               .title("제이미 맥북")
                                               .description("제이미 맥북 팔아요")
                                               .subCategory(전자기기_서브_노트북_카테고리)
                                               .startPrice(new Price(10_000))
                                               .bidUnit(new BidUnit(1_000))
                                               .closingTime(LocalDateTime.now())
                                               .build();
    private Bid 지토가_엔초_경매에_입찰 = new Bid(판매자_엔초_구매자_지토_경매, 지토, new BidPrice(15_000));
    private Bid 엔초가_제이미_경매에_입찰 = new Bid(판매자_제이미_구매자_엔초_경매, 엔초, new BidPrice(15_000));

    protected Long 존재하지_않는_채팅방_아이디 = -999L;
    protected ChatRoom 엔초_지토_채팅방 = new ChatRoom(판매자_엔초_구매자_지토_경매, 지토);
    protected ChatRoom 제이미_엔초_채팅방 = new ChatRoom(판매자_제이미_구매자_엔초_경매, 엔초);

    protected Message 엔초가_지토에게_1시에_보낸_쪽지 = Message.builder()
                                                .chatRoom(엔초_지토_채팅방)
                                                .contents("엔초가 지토에게 1시애 보낸 쪽지")
                                                .writer(엔초)
                                                .receiver(지토)
                                                .build();
    protected Message 제이미가_엔초에게_2시에_보낸_쪽지 = Message.builder()
                                                 .chatRoom(제이미_엔초_채팅방)
                                                 .contents("제이미가 엔초에게 2시애 보낸 쪽지")
                                                 .writer(제이미)
                                                 .receiver(엔초)
                                                 .build();

    @BeforeEach
    void setUp() {
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        profileImageRepository.save(프로필_이미지);
        userRepository.saveAll(List.of(판매자, 구매자, 엔초, 제이미, 지토));

        auctionImageRepository.saveAll(
                List.of(
                        경매_대표_이미지, 대표_이미지가_아닌_경매_이미지,
                        엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지,
                        제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지
                )
        );
        채팅방이_없는_경매.addAuctionImages(List.of(경매_대표_이미지, 대표_이미지가_아닌_경매_이미지));
        판매자_엔초_구매자_지토_경매.addAuctionImages(List.of(엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지));
        판매자_제이미_구매자_엔초_경매.addAuctionImages(List.of(제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지));
        auctionRepository.saveAll(
                List.of(채팅방이_없는_경매, 종료되지_않은_경매, 낙찰자가_없는_경매, 판매자_엔초_구매자_지토_경매, 판매자_제이미_구매자_엔초_경매)
        );

        bidRepository.saveAll(List.of(채팅방_없는_경매_입찰, 지토가_엔초_경매에_입찰, 엔초가_제이미_경매에_입찰));
        채팅방이_없는_경매.updateLastBid(채팅방_없는_경매_입찰);
        판매자_엔초_구매자_지토_경매.updateLastBid(지토가_엔초_경매에_입찰);
        판매자_제이미_구매자_엔초_경매.updateLastBid(엔초가_제이미_경매에_입찰);

        chatRoomRepository.saveAll(List.of(엔초_지토_채팅방, 제이미_엔초_채팅방));

        messageRepository.saveAll(List.of(엔초가_지토에게_1시에_보낸_쪽지, 제이미가_엔초에게_2시에_보낸_쪽지));
    }
}
