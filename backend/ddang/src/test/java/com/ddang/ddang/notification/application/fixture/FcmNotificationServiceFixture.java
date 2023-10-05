package com.ddang.ddang.notification.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.notification.application.dto.CreateNotificationDto;
import com.ddang.ddang.notification.domain.NotificationType;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class FcmNotificationServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaMessageRepository messageRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaDeviceTokenRepository deviceTokenRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaBidRepository bidRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    protected User 메시지_조회자_겸_발신자;
    private User 메시지_수신자;
    private User 새로운_입찰자;
    private User 기기토큰이_없는_사용자;
    protected DeviceToken 기기토큰;
    protected CreateNotificationDto 기기토큰이_없는_사용자의_알림_생성_DTO;
    protected CreateNotificationDto 알림_생성_DTO;

    protected String 알림_메시지_아이디 = "notificationMessageId";

    @BeforeEach
    void setUp() {
        메시지_조회자_겸_발신자 = User.builder()
                            .name("메시지_조회자_겸_발신자")
                            .profileImage(new ProfileImage("upload.png", "store.png"))
                            .reliability(4.7d)
                            .oauthId("12345")
                            .build();
        메시지_수신자 = User.builder()
                      .name("메시지_수신자")
                      .profileImage(new ProfileImage("upload.png", "store.png"))
                      .reliability(4.7d)
                      .oauthId("12347")
                      .build();
        새로운_입찰자 = User.builder()
                      .name("입찰자1")
                      .profileImage(new ProfileImage("upload.png", "store.png"))
                      .reliability(4.7d)
                      .oauthId("56789")
                      .build();
        기기토큰이_없는_사용자 = User.builder()
                           .name("기기토큰이 없는 사용자")
                           .profileImage(new ProfileImage("upload.png", "store.png"))
                           .reliability(4.7d)
                           .oauthId("12234")
                           .build();

        userRepository.save(메시지_조회자_겸_발신자);
        userRepository.save(메시지_수신자);
        userRepository.save(새로운_입찰자);
        userRepository.save(기기토큰이_없는_사용자);

        기기토큰 = new DeviceToken(메시지_조회자_겸_발신자, "deviceToken");

        deviceTokenRepository.save(기기토큰);

        알림_생성_DTO = new CreateNotificationDto(
                NotificationType.MESSAGE,
                메시지_조회자_겸_발신자.getId(),
                "제목",
                "내용",
                "/redirectUrlForNotification",
                "image.png"
        );

        기기토큰이_없는_사용자의_알림_생성_DTO = new CreateNotificationDto(
                NotificationType.MESSAGE,
                기기토큰이_없는_사용자.getId(),
                "제목",
                "내용",
                "/redirectUrl",
                "image.png"
        );

        final Auction 경매 = Auction.builder()
                                  .seller(메시지_조회자_겸_발신자)
                                  .title("경매글")
                                  .description("경매글 설명")
                                  .bidUnit(new BidUnit(100))
                                  .startPrice(new Price(100))
                                  .closingTime(LocalDateTime.now().plusDays(3L))
                                  .build();
        auctionRepository.save(경매);

        final AuctionImage 경매_이미지 = new AuctionImage("upload.jpg", "store.jpg");
        auctionImageRepository.save(경매_이미지);
        경매.addAuctionImages(List.of(경매_이미지));

        final Bid bid = new Bid(경매, 새로운_입찰자, new BidPrice(200));
        bidRepository.save(bid);
        경매.updateLastBid(bid);

        final ChatRoom 채팅방 = new ChatRoom(경매, 메시지_조회자_겸_발신자);
        chatRoomRepository.save(채팅방);

        final Message 메시지 = Message.builder()
                                   .chatRoom(채팅방)
                                   .writer(메시지_조회자_겸_발신자)
                                   .receiver(메시지_수신자)
                                   .contents("메시지")
                                   .build();
        messageRepository.save(메시지);
    }
}
