package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.infrastructure.persistence.JpaAuctionRepository;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.JpaChatRoomRepository;
import com.ddang.ddang.chat.infrastructure.persistence.JpaMessageRepository;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MessageServiceFixture {

    @Autowired
    private JpaMessageRepository messageRepository;

    @Autowired
    private JpaAuctionRepository auctionRepository;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaChatRoomRepository chatRoomRepository;

    @Autowired
    private JpaCategoryRepository categoryRepository;

    protected CreateMessageDto 메시지_생성_DTO;
    protected CreateMessageDto 유효하지_않은_채팅방의_메시지_생성_DTO;
    protected CreateMessageDto 유효하지_않은_발신자의_메시지_생성_DTO;
    protected CreateMessageDto 유효하지_않은_수신자의_메시지_생성_DTO;
    protected CreateMessageDto 수신자가_탈퇴한_경우_메시지_생성_DTO;
    protected ReadMessageRequest 마지막_조회_메시지_아이디가_없는_메시지_조회용_request;
    protected ReadMessageRequest 두_번째_메시지부터_모든_메시지_조회용_request;
    protected ReadMessageRequest 조회할_메시지가_더이상_없는_메시지_조회용_request;
    protected ReadMessageRequest 유효하지_않은_사용자의_메시지_조회용_request;
    protected ReadMessageRequest 유효하지_않은_채팅방의_메시지_조회용_request;
    protected ReadMessageRequest 존재하지_않는_마지막_메시지_아이디의_메시지_조회용_request;

    protected String 이미지_절대_경로 = "/imageUrl";
    protected int 메시지_총_개수 = 10;

    @BeforeEach
    void setUp() {
        final Category 전자기기 = new Category("전자기기");
        final Category 전자기기_하위_노트북 = new Category("노트북");
        전자기기.addSubCategory(전자기기_하위_노트북);
        categoryRepository.save(전자기기);

        final Auction 경매 = Auction.builder()
                                  .title("경매")
                                  .description("description")
                                  .bidUnit(new BidUnit(1_000))
                                  .startPrice(new Price(10_000))
                                  .closingTime(LocalDateTime.now().plusDays(3L))
                                  .build();
        auctionRepository.save(경매);

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
                             .oauthId("12346")
                             .build();
        final User 탈퇴한_사용자 = User.builder()
                                 .name("탈퇴한 사용자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(4.7d)
                                 .oauthId("12347")
                                 .build();
        탈퇴한_사용자.withdrawal();;
        userRepository.saveAll(List.of(발신자, 수신자, 탈퇴한_사용자));

        final ChatRoom 채팅방 = new ChatRoom(경매, 발신자);
        final ChatRoom 탈퇴한_사용자와의_채팅방 = new ChatRoom(경매, 탈퇴한_사용자);
        chatRoomRepository.saveAll(List.of(채팅방, 탈퇴한_사용자와의_채팅방));

        메시지_생성_DTO = new CreateMessageDto(
                채팅방.getId(),
                발신자.getId(),
                수신자.getId(),
                "메시지 내용"
        );
        유효하지_않은_채팅방의_메시지_생성_DTO = new CreateMessageDto(
                -999L,
                발신자.getId(),
                수신자.getId(),
                "메시지 내용"
        );
        유효하지_않은_발신자의_메시지_생성_DTO = new CreateMessageDto(
                채팅방.getId(),
                -999L,
                수신자.getId(),
                "메시지 내용"
        );
        유효하지_않은_수신자의_메시지_생성_DTO = new CreateMessageDto(
                채팅방.getId(),
                발신자.getId(),
                -999L,
                "메시지 내용"
        );
        수신자가_탈퇴한_경우_메시지_생성_DTO = new CreateMessageDto(
                탈퇴한_사용자와의_채팅방.getId(),
                발신자.getId(),
                탈퇴한_사용자.getId(),
                "메시지 내용"
        );

        final List<Message> 메시지들 = new ArrayList<>();
        for (int count = 0; count < 메시지_총_개수; count++) {
            final Message 메시지 = Message.builder()
                                       .writer(발신자)
                                       .receiver(수신자)
                                       .chatRoom(채팅방)
                                       .contents("메시지 내용")
                                       .build();
            메시지들.add(메시지);
            messageRepository.save(메시지);
        }

        마지막_조회_메시지_아이디가_없는_메시지_조회용_request = new ReadMessageRequest(발신자.getId(), 채팅방.getId(), null);
        두_번째_메시지부터_모든_메시지_조회용_request = new ReadMessageRequest(발신자.getId(), 채팅방.getId(), 메시지들.get(0).getId());
        조회할_메시지가_더이상_없는_메시지_조회용_request = new ReadMessageRequest(발신자.getId(), 채팅방.getId(), 메시지들.get(메시지_총_개수 - 1)
                                                                                               .getId());
        유효하지_않은_사용자의_메시지_조회용_request = new ReadMessageRequest(-999L, 채팅방.getId(), null);
        유효하지_않은_채팅방의_메시지_조회용_request = new ReadMessageRequest(발신자.getId(), -999L, null);
        존재하지_않는_마지막_메시지_아이디의_메시지_조회용_request = new ReadMessageRequest(발신자.getId(), 채팅방.getId(), -999L);
    }
}
