package com.ddang.ddang.chat.infrastructure.persistence.fixture;

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
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class QuerydslChatRoomAndMessageAndImageRepositoryImplFixture {

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

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    JpaMessageRepository messageRepository;

    private Category 전자기기_카테고리;
    private Category 전자기기_서브_노트북_카테고리;
    private ProfileImage 프로필_이미지;
    private User 메리;
    protected User 엔초;
    private User 제이미;
    private User 지토;
    private AuctionImage 메리의_대표_이미지가_아닌_경매_이미지;
    private AuctionImage 엔초의_대표_이미지가_아닌_경매_이미지;
    private AuctionImage 제이미의_대표_이미지가_아닌_경매_이미지;
    private Auction 메리의_경매;
    private Auction 엔초의_경매;
    private Auction 제이미의_경매;
    private Bid 엔초가_메리_경매에_입찰;
    private Bid 지토가_엔초_경매에_입찰;
    private Bid 엔초가_제이미_경매에_입찰;
    private Message 제이미가_엔초에게_1시에_보낸_쪽지;
    private Message 엔초가_지토에게_2시에_보낸_쪽지;

    protected AuctionImage 메리의_경매_대표_이미지;
    protected AuctionImage 엔초의_경매_대표_이미지;
    protected AuctionImage 제이미의_경매_대표_이미지;
    protected ChatRoom 메리_엔초_채팅방;
    protected ChatRoom 엔초_지토_채팅방;
    protected ChatRoom 제이미_엔초_채팅방;
    protected Message 메리가_엔초에게_3시에_보낸_쪽지;
    protected Message 제이미가_엔초에게_4시에_보낸_쪽지;
    protected Message 엔초가_지토에게_5시에_보낸_쪽지;

    @BeforeEach
    void setUp() {
        전자기기_카테고리 = new Category("전자기기");
        전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        프로필_이미지 = new ProfileImage("upload.png", "store.png");
        메리 = User.builder()
                  .name("메리")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();
        엔초 = User.builder()
                  .name("엔초")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12346")
                  .build();
        제이미 = User.builder()
                   .name("제이미")
                   .profileImage(프로필_이미지)
                   .reliability(4.7d)
                   .oauthId("12347")
                   .build();
        지토 = User.builder()
                  .name("지토")
                  .profileImage(프로필_이미지)
                  .reliability(4.7d)
                  .oauthId("12348")
                  .build();
        메리의_경매_대표_이미지 = new AuctionImage("메리의_경매_대표_이미지.png", "메리의_경매_대표_이미지.png");
        메리의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("메리의_대표 이미지가_아닌_경매_이미지.png", "메리의_대표 이미지가_아닌_경매_이미지.png");
        엔초의_경매_대표_이미지 = new AuctionImage("엔초의_경매_대표_이미지.png", "엔초의_경매_대표_이미지.png");
        엔초의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("엔초의_대표 이미지가_아닌_경매_이미지.png", "엔초의_대표 이미지가_아닌_경매_이미지.png");
        제이미의_경매_대표_이미지 = new AuctionImage("제이미의_경매_대표_이미지.png", "제이미의_경매_대표_이미지.png");
        제이미의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("제이미의_대표 이미지가_아닌_경매_이미지.png", "제이미의_대표 이미지가_아닌_경매_이미지.png");
        메리의_경매 = Auction.builder()
                          .seller(메리)
                          .title("메리 맥북")
                          .description("메리 맥북 팔아요")
                          .subCategory(전자기기_서브_노트북_카테고리)
                          .startPrice(new Price(10_000))
                          .bidUnit(new BidUnit(1_000))
                          .closingTime(LocalDateTime.now())
                          .build();
        엔초의_경매 = Auction.builder()
                          .seller(엔초)
                          .title("엔초 맥북")
                          .description("엔초 맥북 팔아요")
                          .subCategory(전자기기_서브_노트북_카테고리)
                          .startPrice(new Price(10_000))
                          .bidUnit(new BidUnit(1_000))
                          .closingTime(LocalDateTime.now())
                          .build();
        제이미의_경매 = Auction.builder()
                           .seller(제이미)
                           .title("제이미 맥북")
                           .description("제이미 맥북 팔아요")
                           .subCategory(전자기기_서브_노트북_카테고리)
                           .startPrice(new Price(10_000))
                           .bidUnit(new BidUnit(1_000))
                           .closingTime(LocalDateTime.now())
                           .build();
        엔초가_메리_경매에_입찰 = new Bid(메리의_경매, 엔초, new BidPrice(15_000));
        지토가_엔초_경매에_입찰 = new Bid(엔초의_경매, 지토, new BidPrice(15_000));
        엔초가_제이미_경매에_입찰 = new Bid(제이미의_경매, 엔초, new BidPrice(15_000));

        메리_엔초_채팅방 = new ChatRoom(메리의_경매, 엔초);
        엔초_지토_채팅방 = new ChatRoom(엔초의_경매, 지토);
        제이미_엔초_채팅방 = new ChatRoom(제이미의_경매, 엔초);

        제이미가_엔초에게_1시에_보낸_쪽지 = Message.builder()
                                           .chatRoom(제이미_엔초_채팅방)
                                           .contents("제이미가 엔초에게 1시애 보낸 쪽지")
                                           .writer(제이미)
                                           .receiver(엔초)
                                           .build();
        엔초가_지토에게_2시에_보낸_쪽지 = Message.builder()
                                          .chatRoom(엔초_지토_채팅방)
                                          .contents("엔초가 지토에게 2시애 보낸 쪽지")
                                          .writer(엔초)
                                          .receiver(지토)
                                          .build();
        메리가_엔초에게_3시에_보낸_쪽지 = Message.builder()
                                          .chatRoom(메리_엔초_채팅방)
                                          .contents("메리가 엔초에게 3시에 보낸 쪽지")
                                          .writer(엔초)
                                          .receiver(지토)
                                          .build();
        제이미가_엔초에게_4시에_보낸_쪽지 = Message.builder()
                                           .chatRoom(제이미_엔초_채팅방)
                                           .contents("제이미가 엔초에게 4시애 보낸 쪽지")
                                           .writer(제이미)
                                           .receiver(엔초)
                                           .build();
        엔초가_지토에게_5시에_보낸_쪽지 = Message.builder()
                                          .chatRoom(엔초_지토_채팅방)
                                          .contents("엔초가 지토에게 5시애 보낸 쪽지")
                                          .writer(엔초)
                                          .receiver(지토)
                                          .build();


        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        userRepository.saveAll(List.of(메리, 엔초, 제이미, 지토));

        메리의_경매.addAuctionImages(List.of(메리의_경매_대표_이미지, 메리의_대표_이미지가_아닌_경매_이미지));
        엔초의_경매.addAuctionImages(List.of(엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지));
        제이미의_경매.addAuctionImages(List.of(제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지));
        auctionRepository.saveAll(List.of(메리의_경매, 엔초의_경매, 제이미의_경매));

        bidRepository.saveAll(List.of(엔초가_메리_경매에_입찰, 지토가_엔초_경매에_입찰, 엔초가_제이미_경매에_입찰));
        메리의_경매.updateLastBid(엔초가_메리_경매에_입찰);
        엔초의_경매.updateLastBid(지토가_엔초_경매에_입찰);
        제이미의_경매.updateLastBid(엔초가_제이미_경매에_입찰);

        chatRoomRepository.saveAll(List.of(메리_엔초_채팅방, 엔초_지토_채팅방, 제이미_엔초_채팅방));

        messageRepository.saveAll(
                List.of(
                        제이미가_엔초에게_1시에_보낸_쪽지,
                        엔초가_지토에게_2시에_보낸_쪽지,
                        메리가_엔초에게_3시에_보낸_쪽지,
                        제이미가_엔초에게_4시에_보낸_쪽지,
                        엔초가_지토에게_5시에_보낸_쪽지
                )
        );

        em.flush();
        em.clear();
    }
}
