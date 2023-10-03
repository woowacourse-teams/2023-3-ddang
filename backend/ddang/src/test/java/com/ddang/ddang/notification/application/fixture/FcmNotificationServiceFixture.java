package com.ddang.ddang.notification.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.infrastructure.persistence.JpaBidRepository;
import com.ddang.ddang.chat.application.dto.MessageDto;
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

    protected User 사용자1;
    private User 사용자2;
    private User 입찰자1;
    private User 기기토큰이_없는_사용자;
    protected DeviceToken 기기토큰;
    protected CreateNotificationDto 프로필_이미지가_null인_알림_생성_dto;
    protected CreateNotificationDto 기기토큰이_없는_사용자의_알림_생성_DTO;
    protected CreateNotificationDto 알림_생성_DTO;

    protected String 알림_메시지_아이디 = "notificationMessageId";

    @BeforeEach
    void setUp() {
        사용자1 = User.builder()
                   .name("사용자1")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(4.7d)
                   .oauthId("12345")
                   .build();
        사용자2 = User.builder()
                   .name("사용자2")
                   .profileImage(new ProfileImage("upload.png", "store.png"))
                   .reliability(4.7d)
                   .oauthId("12347")
                   .build();
        입찰자1 = User.builder()
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

        userRepository.save(사용자1);
        userRepository.save(사용자2);
        userRepository.save(입찰자1);
        userRepository.save(기기토큰이_없는_사용자);

        기기토큰 = new DeviceToken(사용자1, "deviceToken");

        deviceTokenRepository.save(기기토큰);

        알림_생성_DTO = new CreateNotificationDto(
                NotificationType.MESSAGE,
                사용자1.getId(),
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
                                  .seller(사용자1)
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

        final Bid bid = new Bid(경매, 입찰자1, new BidPrice(200));
        bidRepository.save(bid);
        경매.updateLastBid(bid);

        final ChatRoom 채팅방 = new ChatRoom(경매, 사용자1);
        chatRoomRepository.save(채팅방);

        final Message 메시지 = Message.builder()
                                   .chatRoom(채팅방)
                                   .writer(사용자1)
                                   .receiver(사용자2)
                                   .contents("메시지")
                                   .build();
        messageRepository.save(메시지);

        final MessageDto 프로필_이미지가_null인_메시지_DTO = MessageDto.of(메시지, 채팅방, 사용자1, 사용자2, null);
        프로필_이미지가_null인_알림_생성_dto = CreateNotificationDto.from(프로필_이미지가_null인_메시지_DTO);
    }
}
