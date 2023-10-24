package com.ddang.ddang.chat.infrastructure.persistence.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.bid.infrastructure.persistence.BidRepositoryImpl;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ChatRoomRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.MessageRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.QuerydslMessageRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class ChatRoomAndMessageAndImageRepositoryImplFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    private AuctionRepository auctionRepository;

    private UserRepository userRepository;

    private BidRepository bidRepository;

    private ChatRoomRepository chatRoomRepository;

    private MessageRepository messageRepository;

    protected User 엔초;
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
    void fixtureSetUp(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaChatRoomRepository jpaChatRoomRepository,
            @Autowired final JpaBidRepository jpaBidRepository,
            @Autowired final JpaMessageRepository jpaMessageRepository
    ) {
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        chatRoomRepository = new ChatRoomRepositoryImpl(jpaChatRoomRepository);
        bidRepository = new BidRepositoryImpl(jpaBidRepository);
        messageRepository = new MessageRepositoryImpl(jpaMessageRepository, new QuerydslMessageRepository(jpaQueryFactory));

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        final ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");

        엔초 = User.builder()
                 .name("엔초")
                 .profileImage(프로필_이미지)
                 .reliability(new Reliability(4.7d))
                 .oauthId("12346")
                 .build();
        final User 메리 = User.builder()
                            .name("메리")
                            .profileImage(프로필_이미지)
                            .reliability(new Reliability(4.7d))
                            .oauthId("12345")
                            .build();
        final User 제이미 = User.builder()
                             .name("제이미")
                             .profileImage(프로필_이미지)
                             .reliability(new Reliability(4.7d))
                             .oauthId("12347")
                             .build();
        final User 지토 = User.builder()
                            .name("지토")
                            .profileImage(프로필_이미지)
                            .reliability(new Reliability(4.7d))
                            .oauthId("12348")
                            .build();

        메리의_경매_대표_이미지 = new AuctionImage("메리의_경매_대표_이미지.png", "메리의_경매_대표_이미지.png");
        final AuctionImage 메리의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("메리의_대표 이미지가_아닌_경매_이미지.png", "메리의_대표 이미지가_아닌_경매_이미지.png");
        엔초의_경매_대표_이미지 = new AuctionImage("엔초의_경매_대표_이미지.png", "엔초의_경매_대표_이미지.png");
        final AuctionImage 엔초의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("엔초의_대표 이미지가_아닌_경매_이미지.png", "엔초의_대표 이미지가_아닌_경매_이미지.png");
        제이미의_경매_대표_이미지 = new AuctionImage("제이미의_경매_대표_이미지.png", "제이미의_경매_대표_이미지.png");
        final AuctionImage 제이미의_대표_이미지가_아닌_경매_이미지 =
                new AuctionImage("제이미의_대표 이미지가_아닌_경매_이미지.png", "제이미의_대표 이미지가_아닌_경매_이미지.png");

        final Auction 메리의_경매 = Auction.builder()
                                      .seller(메리)
                                      .title("메리 맥북")
                                      .description("메리 맥북 팔아요")
                                      .subCategory(전자기기_서브_노트북_카테고리)
                                      .startPrice(new Price(10_000))
                                      .bidUnit(new BidUnit(1_000))
                                      .closingTime(LocalDateTime.now())
                                      .build();
        final Auction 엔초의_경매 = Auction.builder()
                                      .seller(엔초)
                                      .title("엔초 맥북")
                                      .description("엔초 맥북 팔아요")
                                      .subCategory(전자기기_서브_노트북_카테고리)
                                      .startPrice(new Price(10_000))
                                      .bidUnit(new BidUnit(1_000))
                                      .closingTime(LocalDateTime.now())
                                      .build();
        final Auction 제이미의_경매 = Auction.builder()
                                       .seller(제이미)
                                       .title("제이미 맥북")
                                       .description("제이미 맥북 팔아요")
                                       .subCategory(전자기기_서브_노트북_카테고리)
                                       .startPrice(new Price(10_000))
                                       .bidUnit(new BidUnit(1_000))
                                       .closingTime(LocalDateTime.now())
                                       .build();

        final Bid 엔초가_메리_경매에_입찰 = new Bid(메리의_경매, 엔초, new BidPrice(15_000));
        final Bid 지토가_엔초_경매에_입찰 = new Bid(엔초의_경매, 지토, new BidPrice(15_000));
        final Bid 엔초가_제이미_경매에_입찰 = new Bid(제이미의_경매, 엔초, new BidPrice(15_000));

        메리_엔초_채팅방 = new ChatRoom(메리의_경매, 엔초);
        엔초_지토_채팅방 = new ChatRoom(엔초의_경매, 지토);
        제이미_엔초_채팅방 = new ChatRoom(제이미의_경매, 엔초);

        final Message 제이미가_엔초에게_1시에_보낸_쪽지 = Message.builder()
                                                   .chatRoom(제이미_엔초_채팅방)
                                                   .contents("제이미가 엔초에게 1시애 보낸 쪽지")
                                                   .writer(제이미)
                                                   .receiver(엔초)
                                                   .build();
        final Message 엔초가_지토에게_2시에_보낸_쪽지 = Message.builder()
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

        userRepository.save(메리);
        userRepository.save(엔초);
        userRepository.save(제이미);
        userRepository.save(지토);

        메리의_경매.addAuctionImages(List.of(메리의_경매_대표_이미지, 메리의_대표_이미지가_아닌_경매_이미지));
        엔초의_경매.addAuctionImages(List.of(엔초의_경매_대표_이미지, 엔초의_대표_이미지가_아닌_경매_이미지));
        제이미의_경매.addAuctionImages(List.of(제이미의_경매_대표_이미지, 제이미의_대표_이미지가_아닌_경매_이미지));

        auctionRepository.save(메리의_경매);
        auctionRepository.save(엔초의_경매);
        auctionRepository.save(제이미의_경매);

        bidRepository.save(엔초가_메리_경매에_입찰);
        bidRepository.save(지토가_엔초_경매에_입찰);
        bidRepository.save(엔초가_제이미_경매에_입찰);
        메리의_경매.updateLastBid(엔초가_메리_경매에_입찰);
        엔초의_경매.updateLastBid(지토가_엔초_경매에_입찰);
        제이미의_경매.updateLastBid(엔초가_제이미_경매에_입찰);

        chatRoomRepository.save(메리_엔초_채팅방);
        chatRoomRepository.save(엔초_지토_채팅방);
        chatRoomRepository.save(제이미_엔초_채팅방);

        messageRepository.save(제이미가_엔초에게_1시에_보낸_쪽지);
        messageRepository.save(엔초가_지토에게_2시에_보낸_쪽지);
        messageRepository.save(메리가_엔초에게_3시에_보낸_쪽지);
        messageRepository.save(제이미가_엔초에게_4시에_보낸_쪽지);
        messageRepository.save(엔초가_지토에게_5시에_보낸_쪽지);
    }
}
