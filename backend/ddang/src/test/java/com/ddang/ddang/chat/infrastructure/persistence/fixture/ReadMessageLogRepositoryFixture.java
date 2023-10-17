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
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.MessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ChatRoomRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaReadMessageLogRepository;
import com.ddang.ddang.chat.infrastructure.persistence.MessageRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.QuerydslMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ReadMessageLogRepositoryImpl;
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
import java.util.ArrayList;
import java.util.List;

public class ReadMessageLogRepositoryFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    private AuctionRepository auctionRepository;

    private UserRepository userRepository;

    private BidRepository bidRepository;

    private ChatRoomRepository chatRoomRepository;

    private MessageRepository messageRepository;

    private ReadMessageLogRepositoryImpl readMessageLogRepository;

    protected ChatRoom 메리_엔초_채팅방;
    protected User 메리;
    protected User 엔초;
    protected ReadMessageLog 다섯_번째_메시지까지_읽은_메시지_로그;
    protected Message 다섯_번째_메시지;

    protected AuctionImage 메리의_경매_대표_이미지 = new AuctionImage("메리_경매_대표_이미지.png", "메리의_경매_대표_이미지.png");
    protected AuctionImage 메리의_대표_이미지가_아닌_경매_이미지 = new AuctionImage("메리의_대표 이미지가_아닌_경매_이미지.png", "메리의_대표 이미지가_아닌_경매_이미지.png");
    protected ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");

    @BeforeEach
    void fixtureSetUp(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaBidRepository jpaBidRepository,
            @Autowired final JpaChatRoomRepository jpaChatRoomRepository,
            @Autowired final JpaMessageRepository jpaMessageRepository,
            @Autowired final JpaReadMessageLogRepository jpaReadMessageLogRepository
    ) {
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        bidRepository = new BidRepositoryImpl(jpaBidRepository);
        chatRoomRepository = new ChatRoomRepositoryImpl(jpaChatRoomRepository);
        messageRepository = new MessageRepositoryImpl(jpaMessageRepository, new QuerydslMessageRepository(jpaQueryFactory));
        readMessageLogRepository = new ReadMessageLogRepositoryImpl(jpaReadMessageLogRepository);

        final Category 전자기기_카테고리 = new Category("전자기기");
        final Category 전자기기_서브_노트북_카테고리 = new Category("노트북 카테고리");
        전자기기_카테고리.addSubCategory(전자기기_서브_노트북_카테고리);
        categoryRepository.save(전자기기_카테고리);

        메리 = User.builder()
                 .name("메리_판매자")
                 .profileImage(프로필_이미지)
                 .reliability(new Reliability(4.7d))
                 .oauthId("12345")
                 .build();
        엔초 = User.builder()
                 .name("엔초_구매자")
                 .profileImage(프로필_이미지)
                 .reliability(new Reliability(4.7d))
                 .oauthId("14567")
                 .build();
        userRepository.save(메리);
        userRepository.save(엔초);

        final Auction 판매자_메리_구매자_엔초_경매 = Auction.builder()
                                                .seller(메리)
                                                .title("메리 맥북")
                                                .description("메리 맥북 팔아요")
                                                .subCategory(전자기기_서브_노트북_카테고리)
                                                .startPrice(new Price(10_000))
                                                .bidUnit(new BidUnit(1_000))
                                                .closingTime(LocalDateTime.now())
                                                .build();
        판매자_메리_구매자_엔초_경매.addAuctionImages(List.of(메리의_경매_대표_이미지, 메리의_대표_이미지가_아닌_경매_이미지));
        auctionRepository.save(판매자_메리_구매자_엔초_경매);


        final Bid 엔초가_메리_경매에_입찰 = new Bid(판매자_메리_구매자_엔초_경매, 엔초, new BidPrice(15_000));
        bidRepository.save(엔초가_메리_경매에_입찰);

        판매자_메리_구매자_엔초_경매.updateLastBid(엔초가_메리_경매에_입찰);
        메리_엔초_채팅방 = new ChatRoom(판매자_메리_구매자_엔초_경매, 엔초);
        chatRoomRepository.save(메리_엔초_채팅방);

        List<Message> 메리_엔초_메시지들 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Message 메시지 = Message.builder()
                                       .chatRoom(메리_엔초_채팅방)
                                       .writer(메리)
                                       .receiver(엔초)
                                       .contents("안녕하세요")
                                       .build();
            messageRepository.save(메시지);
            메리_엔초_메시지들.add(메시지);
        }

        다섯_번째_메시지 = 메리_엔초_메시지들.get(4);
        다섯_번째_메시지까지_읽은_메시지_로그 = new ReadMessageLog(메리_엔초_채팅방, 메리);
        다섯_번째_메시지까지_읽은_메시지_로그.updateLastReadMessage(다섯_번째_메시지.getId());

        final ReadMessageLog 메리_엔초_채팅방의_메리_메시지_조회_로그 = new ReadMessageLog(메리_엔초_채팅방, 메리);
        메리_엔초_채팅방의_메리_메시지_조회_로그.updateLastReadMessage(다섯_번째_메시지.getId());
        readMessageLogRepository.save(메리_엔초_채팅방의_메리_메시지_조회_로그);
    }
}
