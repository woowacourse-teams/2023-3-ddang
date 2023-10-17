package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.AuctionRepositoryImpl;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.auction.infrastructure.persistence.QuerydslAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.BidRepositoryImpl;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ChatRoomRepositoryImpl;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaReadMessageLogRepository;
import com.ddang.ddang.chat.infrastructure.persistence.ReadMessageLogRepositoryImpl;
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

@SuppressWarnings("NonAsciiCharacters")
public class LastReadMessageLogServiceFixture {

    @Autowired
    private JpaCategoryRepository categoryRepository;

    private AuctionRepository auctionRepository;

    private UserRepository userRepository;

    private ChatRoomRepository chatRoomRepository;

    private BidRepositoryImpl bidRepository;

    private ReadMessageLogRepository readMessageLogRepository;

    protected CreateReadMessageLogEvent 메시지_로그_생성용_이벤트;
    protected UpdateReadMessageLogEvent 메시지_로그_업데이트용_이벤트;
    protected UpdateReadMessageLogEvent 유효하지_않는_메시지_조회_로그;
    protected User 메시지_로그_생성용_발신자_겸_판매자;
    protected User 메시지_로그_생성용_입찰자_구매자;
    protected User 메시지_로그_업데이트용_발신자_겸_판매자;
    protected User 메시지_로그_업데이트용_입찰자;
    protected User 저장되지_않은_사용자;
    protected Auction 메시지_로그_생성용_경매;
    protected Message 메시지_로그_생성용_마지막_조회_메시지;
    protected Message 메시지_로그_업데이트용_마지막_조회_메시지;
    protected Auction 메시지_로그_업데이트용_경매;
    protected ChatRoom 메시지_로그_생성용_채팅방;
    protected ChatRoom 저장되지_않은_채팅방;

    @BeforeEach
    void fixtureSetUp(
            @Autowired final JPAQueryFactory jpaQueryFactory,
            @Autowired final JpaAuctionRepository jpaAuctionRepository,
            @Autowired final JpaUserRepository jpaUserRepository,
            @Autowired final JpaChatRoomRepository jpaChatRoomRepository,
            @Autowired final JpaBidRepository jpaBidRepository,
            @Autowired final JpaReadMessageLogRepository jpaReadMessageLogRepository
    ) {
        auctionRepository = new AuctionRepositoryImpl(jpaAuctionRepository, new QuerydslAuctionRepository(jpaQueryFactory));
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        chatRoomRepository = new ChatRoomRepositoryImpl(jpaChatRoomRepository);
        bidRepository = new BidRepositoryImpl(jpaBidRepository);
        readMessageLogRepository = new ReadMessageLogRepositoryImpl(jpaReadMessageLogRepository);

        final Category 전자기기 = new Category("전자기기");
        final Category 전자기기_하위_노트북 = new Category("노트북");
        전자기기.addSubCategory(전자기기_하위_노트북);
        categoryRepository.save(전자기기);

        메시지_로그_생성용_발신자_겸_판매자 = User.builder()
                                   .name("메시지_로그_생성용_발신자")
                                   .profileImage(new ProfileImage("upload.png", "store.png"))
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12345")
                                   .build();
        메시지_로그_생성용_입찰자_구매자 = User.builder()
                                 .name("메시지_로그_생성용_입찰자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12346")
                                 .build();
        메시지_로그_업데이트용_발신자_겸_판매자 = User.builder()
                                     .name("메시지_로그_업데이트용_발신자")
                                     .profileImage(new ProfileImage("upload.png", "store.png"))
                                     .reliability(new Reliability(4.7d))
                                     .oauthId("12347")
                                     .build();
        메시지_로그_업데이트용_입찰자 = User.builder()
                               .name("메시지_로그_업데이트용_입찰자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(new Reliability(4.7d))
                               .oauthId("12348")
                               .build();
        저장되지_않은_사용자 = User.builder()
                          .name("저장되지_않은_사용자")
                          .profileImage(new ProfileImage("upload.png", "store.png"))
                          .reliability(new Reliability(4.7d))
                          .oauthId("12349")
                          .build();
        userRepository.save(메시지_로그_생성용_발신자_겸_판매자);
        userRepository.save(메시지_로그_생성용_입찰자_구매자);
        userRepository.save(메시지_로그_업데이트용_발신자_겸_판매자);
        userRepository.save(메시지_로그_업데이트용_입찰자);

        메시지_로그_생성용_경매 = Auction.builder()
                               .title("경매")
                               .seller(메시지_로그_생성용_발신자_겸_판매자)
                               .description("description")
                               .bidUnit(new BidUnit(1_000))
                               .startPrice(new Price(10_000))
                               .closingTime(LocalDateTime.now().plusDays(3L))
                               .build();
        메시지_로그_업데이트용_경매 = Auction.builder()
                                 .seller(메시지_로그_업데이트용_발신자_겸_판매자)
                                 .title("메시지_로그_업데이트용_경매")
                                 .description("description")
                                 .bidUnit(new BidUnit(1_000))
                                 .startPrice(new Price(10_000))
                                 .closingTime(LocalDateTime.now().plusDays(3L))
                                 .build();
        auctionRepository.save(메시지_로그_생성용_경매);
        auctionRepository.save(메시지_로그_업데이트용_경매);

        final Bid 메시지_로그_생성용_입찰 = new Bid(메시지_로그_생성용_경매, 메시지_로그_생성용_입찰자_구매자, new BidPrice(15_000));
        final Bid 메시지_로그_업데이트용_입찰 = new Bid(메시지_로그_업데이트용_경매, 메시지_로그_업데이트용_입찰자, new BidPrice(15_000));
        bidRepository.save(메시지_로그_생성용_입찰);
        bidRepository.save(메시지_로그_업데이트용_입찰);
        메시지_로그_생성용_경매.updateLastBid(메시지_로그_생성용_입찰);
        메시지_로그_업데이트용_경매.updateLastBid(메시지_로그_업데이트용_입찰);

        메시지_로그_생성용_채팅방 = new ChatRoom(메시지_로그_생성용_경매, 메시지_로그_생성용_입찰자_구매자);
        저장되지_않은_채팅방 = new ChatRoom(메시지_로그_생성용_경매, 메시지_로그_생성용_입찰자_구매자);
        final ChatRoom 메시지_로그_업데이트용_채팅방 = new ChatRoom(메시지_로그_업데이트용_경매, 메시지_로그_업데이트용_발신자_겸_판매자);
        chatRoomRepository.save(메시지_로그_생성용_채팅방);
        chatRoomRepository.save(메시지_로그_업데이트용_채팅방);

        메시지_로그_생성용_이벤트 = new CreateReadMessageLogEvent(메시지_로그_생성용_채팅방);
        메시지_로그_업데이트용_이벤트 = new UpdateReadMessageLogEvent(메시지_로그_업데이트용_발신자_겸_판매자, 메시지_로그_업데이트용_채팅방, 메시지_로그_업데이트용_마지막_조회_메시지);

        메시지_로그_생성용_마지막_조회_메시지 = Message.builder()
                                       .writer(메시지_로그_생성용_발신자_겸_판매자)
                                       .receiver(메시지_로그_생성용_입찰자_구매자)
                                       .contents("메시지")
                                       .build();
        메시지_로그_업데이트용_마지막_조회_메시지 = Message.builder()
                                         .writer(메시지_로그_업데이트용_발신자_겸_판매자)
                                         .receiver(메시지_로그_업데이트용_입찰자)
                                         .contents("메시지")
                                         .build();
        final Message 저장되지_않은_메시지 = Message.builder()
                                           .writer(저장되지_않은_사용자)
                                           .contents("저장되지 않은 메시지")
                                           .build();
        메시지_로그_업데이트용_이벤트 = new UpdateReadMessageLogEvent(메시지_로그_업데이트용_발신자_겸_판매자, 메시지_로그_업데이트용_채팅방, 메시지_로그_업데이트용_마지막_조회_메시지);

        final ReadMessageLog 메시지_로그_업데이트용_로그 = new ReadMessageLog(메시지_로그_업데이트용_채팅방, 메시지_로그_업데이트용_발신자_겸_판매자);
        readMessageLogRepository.save(메시지_로그_업데이트용_로그);

        유효하지_않는_메시지_조회_로그 = new UpdateReadMessageLogEvent(저장되지_않은_사용자, 저장되지_않은_채팅방, 저장되지_않은_메시지);
    }
}
