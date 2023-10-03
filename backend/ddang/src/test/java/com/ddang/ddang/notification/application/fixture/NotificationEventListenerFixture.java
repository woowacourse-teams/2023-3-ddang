package com.ddang.ddang.notification.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaAuctionImageRepository;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationEventListenerFixture {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaAuctionImageRepository auctionImageRepository;

    protected String 이미지_절대_경로 = "/imageUrl";
    protected CreateMessageDto 메시지_생성_DTO;

    @BeforeEach
    void setUp() {
        final User 발신자 = User.builder()
                             .name("발신자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(4.7d)
                             .oauthId("12345")
                             .build();
        final User 수신자 = User.builder()
                             .name("수신자")
                             .profileImage(new ProfileImage("upload.png", "store.png"))
                             .reliability(4.7d)
                             .oauthId("12347")
                             .build();
        userRepository.save(발신자);
        userRepository.save(수신자);

        final Auction 경매 = Auction.builder()
                                  .seller(발신자)
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

        final ChatRoom 채팅방 = new ChatRoom(경매, 발신자);
        chatRoomRepository.save(채팅방);

        메시지_생성_DTO = new CreateMessageDto(
                채팅방.getId(),
                발신자.getId(),
                수신자.getId(),
                "메시지 내용"
        );
    }
}
