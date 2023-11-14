package com.ddang.ddang.notification.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.bid.application.dto.BidDto;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.event.BidNotificationEvent;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.domain.repository.BidRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.event.MessageNotificationEvent;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.repository.ChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.qna.application.event.AnswerNotificationEvent;
import com.ddang.ddang.qna.application.event.QuestionNotificationEvent;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.domain.repository.RegionRepository;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationEventListenerFixture {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private JpaMessageRepository messageRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    protected CreateMessageDto 메시지_생성_DTO;
    protected CreateBidDto 입찰_생성_DTO;
    protected MessageNotificationEvent 메시지_알림_이벤트;
    protected BidNotificationEvent 입찰_알림_이벤트;
    protected QuestionNotificationEvent 질문_알림_이벤트;
    protected AnswerNotificationEvent 답변_알림_이벤트;

    protected String 이미지_절대_경로 = "/imageUrl";

    @BeforeEach
    void setUpFixture() {
        final Region 서울특별시 = new Region("서울특별시");
        final Region 강남구 = new Region("강남구");
        final Region 역삼동 = new Region("역삼동");

        서울특별시.addSecondRegion(강남구);
        강남구.addThirdRegion(역삼동);

        regionRepository.save(서울특별시);

        final Category 가구_카테고리 = new Category("가구");
        final Category 가구_서브_의자_카테고리 = new Category("의자");

        가구_카테고리.addSubCategory(가구_서브_의자_카테고리);

        categoryRepository.save(가구_카테고리);

        final User 발신자_겸_판매자 = User.builder()
                                   .name("발신자 겸 판매자")
                                   .profileImage(new ProfileImage("upload.png", "store.png"))
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12345")
                                   .build();
        final User 수신자_겸_기존_입찰자 = User.builder()
                                      .name("수신자 겸 기존 입찰자")
                                      .profileImage(new ProfileImage("upload.png", "store.png"))
                                      .reliability(new Reliability(4.7d))
                                      .oauthId("12347")
                                      .build();
        final User 새로운_입찰자 = User.builder()
                                 .name("새로운 입찰자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("13579")
                                 .build();
        final User 질문자 = User.builder()
                             .name("질문자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(new Reliability(4.7d))
                             .oauthId("12038")
                             .build();
        userRepository.save(발신자_겸_판매자);
        userRepository.save(수신자_겸_기존_입찰자);
        userRepository.save(새로운_입찰자);
        userRepository.save(질문자);

        final Auction 경매 = Auction.builder()
                                  .seller(발신자_겸_판매자)
                                  .title("경매글")
                                  .description("경매글 설명")
                                  .bidUnit(new BidUnit(100))
                                  .startPrice(new Price(100))
                                  .closingTime(LocalDateTime.now().plusDays(3L))
                                  .subCategory(가구_서브_의자_카테고리)
                                  .build();

        경매.addAuctionRegions(List.of(new AuctionRegion(역삼동)));

        auctionRepository.save(경매);

        final AuctionImage 경매_이미지 = new AuctionImage("upload.jpg", "store.jpg");
        경매.addAuctionImages(List.of(경매_이미지));
        final ChatRoom 채팅방 = new ChatRoom(경매, 발신자_겸_판매자);
        final Bid bid = new Bid(경매, 수신자_겸_기존_입찰자, new BidPrice(200));
        경매.updateLastBid(bid);

        auctionImageRepository.save(경매_이미지);
        chatRoomRepository.save(채팅방);
        bidRepository.save(bid);

        메시지_생성_DTO = new CreateMessageDto(채팅방.getId(), 발신자_겸_판매자.getId(), 수신자_겸_기존_입찰자.getId(), "메시지 내용");
        입찰_생성_DTO = new CreateBidDto(경매.getId(), 1000, 새로운_입찰자.getId());

        final Message 저장된_메시지 = messageRepository.save(
                Message.builder()
                       .chatRoom(채팅방)
                       .writer(발신자_겸_판매자)
                       .receiver(수신자_겸_기존_입찰자)
                       .contents("메시지 내용")
                       .build()
        );
        final AuctionAndImageDto auctionAndImageDto = new AuctionAndImageDto(경매, 경매_이미지);
        final BidDto 입찰_DTO = new BidDto(수신자_겸_기존_입찰자.getId(), 경매, 이미지_절대_경로);

        메시지_알림_이벤트 = new MessageNotificationEvent(저장된_메시지, 이미지_절대_경로);
        입찰_알림_이벤트 = new BidNotificationEvent(입찰_DTO);

        final Question 질문 = new Question(경매, 질문자, "질문 내용");
        final Answer 답변 = new Answer(발신자_겸_판매자, "응답 내용");
        질문.addAnswer(답변);

        질문_알림_이벤트 = new QuestionNotificationEvent(질문, 이미지_절대_경로);
        답변_알림_이벤트 = new AnswerNotificationEvent(답변, 이미지_절대_경로);
    }
}
